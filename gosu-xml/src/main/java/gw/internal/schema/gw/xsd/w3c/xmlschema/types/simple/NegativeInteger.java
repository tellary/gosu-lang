package gw.internal.schema.gw.xsd.w3c.xmlschema.types.simple;

/***************************************************************************/
/* THIS IS AUTOGENERATED CODE - DO NOT MODIFY OR YOUR CHANGES WILL BE LOST */
/* THIS CODE CAN BE REGENERATED USING 'xsd-codegen'                        */
/***************************************************************************/
public class NegativeInteger extends gw.internal.schema.gw.xsd.w3c.xmlschema.types.simple.NonPositiveInteger implements gw.internal.xml.IXmlGeneratedClass {

  public static final javax.xml.namespace.QName $QNAME = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "negativeInteger", "xs" );
  public static final gw.util.concurrent.LockingLazyVar<gw.lang.reflect.IType> TYPE = new gw.util.concurrent.LockingLazyVar<gw.lang.reflect.IType>( gw.lang.reflect.TypeSystem.getGlobalLock() ) {
          @Override
          protected gw.lang.reflect.IType init() {
            return gw.lang.reflect.TypeSystem.getByFullName( "gw.xsd.w3c.xmlschema.types.simple.NegativeInteger" );
          }
        };
  private static final gw.util.concurrent.LockingLazyVar<java.lang.Object> SCHEMAINFO = new gw.util.concurrent.LockingLazyVar<java.lang.Object>( gw.lang.reflect.TypeSystem.getGlobalLock() ) {
          @Override
          protected java.lang.Object init() {
            gw.lang.reflect.IType type = TYPE.get();
            return getSchemaInfoByType( type );
          }
        };

  public NegativeInteger() {
    super( TYPE.get(), SCHEMAINFO.get() );
  }

  protected NegativeInteger( gw.lang.reflect.IType type, java.lang.Object schemaInfo ) {
    super( type, schemaInfo );
  }

  public NegativeInteger( java.math.BigInteger value ) {
    this();
    TYPE.get().getTypeInfo().getProperty( "$Value" ).getAccessor().setValue( this, value );
  }


  @Deprecated
  public java.math.BigInteger getValue$$gw_xsd_w3c_xmlschema_types_simple_NonPositiveInteger() {
    return super.getValue$$gw_xsd_w3c_xmlschema_types_simple_NonPositiveInteger();
  }

  @Deprecated
  public void setValue$$gw_xsd_w3c_xmlschema_types_simple_NonPositiveInteger( java.math.BigInteger param ) {
    super.setValue$$gw_xsd_w3c_xmlschema_types_simple_NonPositiveInteger( param );
  }

  public java.math.BigInteger getValue$$gw_xsd_w3c_xmlschema_types_simple_NegativeInteger() {
    return (java.math.BigInteger) TYPE.get().getTypeInfo().getProperty( "$Value" ).getAccessor().getValue( this );
  }

  public void setValue$$gw_xsd_w3c_xmlschema_types_simple_NegativeInteger( java.math.BigInteger param ) {
    TYPE.get().getTypeInfo().getProperty( "$Value" ).getAccessor().setValue( this, param );
  }

  @SuppressWarnings( {"UnusedDeclaration"} )
  private static final long FINGERPRINT = 2110283714877373226L;

}
