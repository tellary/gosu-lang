/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.config.CommonServices;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.DelegateFunctionSymbol;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.TemplateRenderFunctionSymbol;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.VarPropertyGetFunctionSymbol;
import gw.internal.gosu.parser.VarPropertySetFunctionSymbol;
import gw.internal.gosu.parser.expressions.ArrayAccess;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.ImplicitTypeAsExpression;
import gw.internal.gosu.parser.expressions.MemberAccess;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.parser.expressions.NullExpression;
import gw.internal.gosu.parser.expressions.NumericLiteral;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.parser.statements.ArrayAssignmentStatement;
import gw.internal.gosu.parser.statements.BeanMethodCallStatement;
import gw.internal.gosu.parser.statements.MemberAssignmentStatement;
import gw.internal.gosu.parser.statements.ReturnStatement;
import gw.internal.gosu.parser.statements.StatementList;
import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.statement.IRReturnStatement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class SyntheticFunctionStatementTransformer extends AbstractStatementTransformer<SyntheticFunctionStatement>
{
  private DynamicFunctionSymbol _dfs;

  public static IRStatement compile( DynamicFunctionSymbol dfs, TopLevelTransformationContext cc, SyntheticFunctionStatement stmt )
  {
    SyntheticFunctionStatementTransformer gen = new SyntheticFunctionStatementTransformer( dfs, cc, stmt );
    return gen.compile();
  }

  private SyntheticFunctionStatementTransformer( DynamicFunctionSymbol dfs, TopLevelTransformationContext cc, SyntheticFunctionStatement stmt )
  {
    super( cc, stmt );
    _dfs = dfs;
  }

  @Override
  protected IRStatement compile_impl()
  {
    if( _dfs instanceof VarPropertyGetFunctionSymbol )
    {
      return compileReturnStatementForGetter();
    }
    else if( _dfs instanceof VarPropertySetFunctionSymbol )
    {
      return compileAssignmentStatementForSetter();
    }
    else if( _dfs instanceof DelegateFunctionSymbol )
    {
      return compileDelegatedMethod();
    }
    else if( _dfs instanceof TemplateRenderFunctionSymbol )
    {
      return compileForwardingMethod();
    }
    else
    {
      throw new UnsupportedOperationException( "Found a SyntheticFunctionStatement with an unexpected dynamic function symbol type " + _dfs.getClass() );
    }
  }

  private IRStatement compileForwardingMethod()
  {
    TemplateRenderFunctionSymbol forwardingDfs = (TemplateRenderFunctionSymbol)_dfs;
    BeanMethodCallExpression methodCall = new BeanMethodCallExpression();
    methodCall.setMethodDescriptor( forwardingDfs.getMi() );
    methodCall.setType( forwardingDfs.getReturnType() );

    List<Statement> stmts = new ArrayList<Statement>();
    IParameterInfo[] targetMethodParams = forwardingDfs.getMi().getParameters();
    List<Expression> args = new ArrayList<Expression>();
    args.add( new TypeLiteral( forwardingDfs.getTemplateType() ) );
    for( int i = 0; i < forwardingDfs.getArgs().size(); i++ )
    {
      if( i+2 == targetMethodParams.length &&
          targetMethodParams[i+1].getFeatureType().isArray() )
      {
        // Handle var-arg argument

        Symbol forwarding_var_args_ = new Symbol( "_forwarding_var_args_", JavaTypes.OBJECT().getArrayType(), null );
        forwarding_var_args_.setIndex( 1 );
        VarStatement arrayVarStmt = new VarStatement();
        arrayVarStmt.setSymbol( forwarding_var_args_ );
        NewExpression arrayInit = new NewExpression();
        arrayInit.setType( forwarding_var_args_.getType() );
        int iSize = forwardingDfs.getArgs().size() - i;
        arrayInit.addSizeExpression( new NumericLiteral( String.valueOf( iSize ), iSize, JavaTypes.pINT() ) );
        arrayVarStmt.setAsExpression( arrayInit );
        stmts.add( arrayVarStmt );

        Identifier array = new Identifier();
        array.setSymbol( forwarding_var_args_, null );
        array.setType( JavaTypes.OBJECT().getArrayType() );
        for( int j = i; j < forwardingDfs.getArgs().size(); j++ )
        {
          Expression argId = new Identifier();
          ISymbol argSym = forwardingDfs.getArgs().get( j );
          ((Identifier)argId).setSymbol( argSym, null );
          argId.setType( ((Identifier)argId).getSymbol().getType() );
          if( argSym.getType().isPrimitive() )
          {
            argId = box( argId );
          }

          ArrayAccess access = new ArrayAccess();
          access.setRootExpression( array );
          access.setMemberExpression( new NumericLiteral( String.valueOf( j - i ), j - i, JavaTypes.pINT() ) );
          access.setType( JavaTypes.OBJECT() );

          ArrayAssignmentStatement assignmentStmt = new ArrayAssignmentStatement();
          assignmentStmt.setArrayAccessExpression( access );
          assignmentStmt.setExpression( argId );

          stmts.add( assignmentStmt );
        }
        args.add( array );
        break;
      }
      else
      {
        Identifier argId = new Identifier();
        argId.setSymbol( forwardingDfs.getArgs().get( i ), null );
        argId.setType( argId.getSymbol().getType() );
        args.add( argId );
      }
    }

    if( args.size() < targetMethodParams.length )
    {
      // Handle case where template has no args
      args.add( new NullExpression() );
    }

    methodCall.setArgs( args.toArray( new Expression[args.size()] ) );

    if( _dfs.getReturnType() != JavaTypes.pVOID() )
    {
      ReturnStatement returnStatement = new ReturnStatement();
      returnStatement.setValue( methodCall );
      stmts.add( returnStatement );
    }
    else
    {
      BeanMethodCallStatement methodCallStatement = new BeanMethodCallStatement();
      methodCallStatement.setBeanMethodCall( methodCall );
      stmts.add( methodCallStatement );
    }

    StatementList stmtList = new StatementList( new StandardSymbolTable() );
    stmtList.setStatements( stmts );
    return StatementListTransformer.compile( _cc(), stmtList );
  }

  private Expression box( Expression expr )
  {
    ImplicitTypeAsExpression cast = new ImplicitTypeAsExpression();
    cast.setLHS( expr );
    cast.setCoercer( CommonServices.getCoercionManager().resolveCoercerStatically( JavaTypes.OBJECT(), expr.getType() ) );
    cast.setType( TypeSystem.getBoxType( expr.getType() ) );
    return cast;
  }

  private IRStatement compileAssignmentStatementForSetter()
  {
    VarPropertySetFunctionSymbol setter = (VarPropertySetFunctionSymbol)_dfs;

    final ISymbol valueSymbol = setter.getArgs().get( 0 );
    return buildFieldSet( getDescriptor( getGosuClass() ),
                          setter.getVarIdentifier().toString(),
                          getDescriptor( valueSymbol.getType() ),
                          (setter.isStatic() ? null : pushThis()),
                          identifier( _cc().getSymbol( valueSymbol.getName() ) ) );
  }

  private IRStatement compileReturnStatementForGetter()
  {
    VarPropertyGetFunctionSymbol getter = (VarPropertyGetFunctionSymbol)_dfs;

    IRExpression returnValue = buildFieldGet( getDescriptor( getGosuClass() ),
                                              getter.getVarIdentifier().toString(),
                                              getDescriptor( getter.getReturnType() ),
                                              (getter.isStatic() ? null : pushThis()) );
    return new IRReturnStatement( null, returnValue );
  }

  private IRStatement compileDelegatedMethod()
  {
    DelegateFunctionSymbol delegateSymbol = (DelegateFunctionSymbol)_dfs;
    Identifier id = new Identifier();
    ISymbol varSymbol = getGosuClass().getMemberField( delegateSymbol.getDelegateStmt().getIdentifierName() ).getSymbol();
    id.setSymbol( varSymbol, null );
    id.setType( varSymbol.getType() );

    if( delegateSymbol.getName().startsWith( "@" ) )
    {
      String propertyName = delegateSymbol.getDisplayName().substring( 1 );
      if( delegateSymbol.getArgs().isEmpty() )
      {
        // Getter
        MemberAccess memberAccess = new MemberAccess();
        memberAccess.setRootExpression( id );
        memberAccess.setMemberName( propertyName );
        memberAccess.setType( delegateSymbol.getReturnType() );

        ReturnStatement returnStatement = new ReturnStatement();
        returnStatement.setValue( memberAccess );

        return ReturnStatementTransformer.compile( _cc(), returnStatement );
      }
      else
      {
        // Setter
        Identifier argId = new Identifier();
        argId.setSymbol( delegateSymbol.getArgs().get( 0 ), null );
        argId.setType( argId.getSymbol().getType() );

        MemberAssignmentStatement memberAssignment = new MemberAssignmentStatement();
        memberAssignment.setRootExpression( id );
        memberAssignment.setMemberName( propertyName );
        memberAssignment.setExpression( argId );

        return MemberAssignmentStatementTransformer.compile( _cc(), memberAssignment );
      }
    }
    else
    {
      /// Normal method call
      BeanMethodCallExpression methodCall = new BeanMethodCallExpression();
      methodCall.setRootExpression( id );
      methodCall.setMethodDescriptor( delegateSymbol.getMi() );
      methodCall.setType( delegateSymbol.getReturnType() );
      // PL-21982
      methodCall.setFunctionType( new FunctionType(delegateSymbol.getMi()));

      Expression[] args = new Expression[delegateSymbol.getArgs().size()];
      for( int i = 0; i < delegateSymbol.getArgs().size(); i++ )
      {
        Identifier argId = new Identifier();
        argId.setSymbol( delegateSymbol.getArgs().get( i ), null );
        argId.setType( argId.getSymbol().getType() );
        args[i] = argId;
      }
      methodCall.setArgs( args );

      if( _dfs.getReturnType() != JavaTypes.pVOID() )
      {
        ReturnStatement returnStatement = new ReturnStatement();
        returnStatement.setValue( methodCall );
        return ReturnStatementTransformer.compile( _cc(), returnStatement );
      }
      else
      {
        BeanMethodCallStatement methodCallStatement = new BeanMethodCallStatement();
        methodCallStatement.setBeanMethodCall( methodCall );
        return BeanMethodCallStatementTransformer.compile( _cc(), methodCallStatement );
      }
    }
  }
}