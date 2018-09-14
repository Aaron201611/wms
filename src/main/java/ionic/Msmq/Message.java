package ionic.Msmq;

import java.io.UnsupportedEncodingException;

public class Message
{
  private static String _encoding = "UTF-16LE";
  private static String _utf8 = "UTF-8";
  byte[] _messageBody;
  String _label;
  byte[] _correlationId;
  boolean _highPriority;

  public void setBodyAsString(String value)
    throws UnsupportedEncodingException
  {
    this._messageBody = value.getBytes(_encoding);
  }

  public String getBodyAsString()
    throws UnsupportedEncodingException
  {
    return new String(this._messageBody, _encoding);
  }
  
  public String getBodyAsString(String format) throws UnsupportedEncodingException{
	  return new String(this._messageBody, format);
  }

  public void setCorrelationIdAsString(String value)
    throws UnsupportedEncodingException
  {
    this._correlationId = value.getBytes(_utf8);
  }

  public String getCorrelationIdAsString()
    throws UnsupportedEncodingException
  {
    return new String(this._correlationId, _utf8);
  }

  public void setBody(byte[] value)
  {
    this._messageBody = value;
  }

  public byte[] getBody()
  {
    return this._messageBody;
  }

  public void setLabel(String value)
  {
    this._label = value;
  }

  public String getLabel()
  {
    return this._label;
  }

  public void setCorrelationId(byte[] value)
  {
    this._correlationId = value;
  }

  public byte[] getCorrelationId()
  {
    return this._correlationId;
  }

  public void setHighPriority(boolean value)
  {
    this._highPriority = value;
  }

  public boolean getHighPriority()
  {
    return this._highPriority;
  }

  Message()
  {
  }

  public Message(String body)
    throws UnsupportedEncodingException
  {
    this(body, "", "");
  }

  public Message(String body, String label, String correlationId)
    throws UnsupportedEncodingException
  {
    this(body.getBytes(_encoding), label, correlationId.getBytes(_encoding));
  }

  public Message(String body, String label, byte[] correlationId)
    throws UnsupportedEncodingException
  {
    this(body.getBytes(_encoding), label, correlationId);
  }

  public Message(byte[] body)
    throws UnsupportedEncodingException
  {
    this(body, "", "");
  }

  public Message(byte[] body, String label, String correlationId)
    throws UnsupportedEncodingException
  {
    this(body, label, correlationId.getBytes(_encoding));
  }

  public Message(byte[] body, String label, byte[] correlationId)
  {
    this._messageBody = body;
    this._label = label;
    this._correlationId = correlationId;
  }
}