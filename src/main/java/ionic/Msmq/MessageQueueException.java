package ionic.Msmq;

public class MessageQueueException extends Exception
{
  public int hresult;

  public MessageQueueException(int HRESULT)
  {
    this.hresult = HRESULT;
  }

  public MessageQueueException(String message, int HRESULT) {
    super(message);
    this.hresult = HRESULT;
  }

  public String toString()
  {
    String msg = getLocalizedMessage();
    String hr = "hr=" + HrToString(this.hresult);
    return msg + " (" + hr + ")";
  }

  private static String HrToString(int hr)
  {
    if (hr == 0)
      return "SUCCESS";
    if (hr == -1072824318)
      return "MQ_ERROR_PROPERTY";
    if (hr == -1072824317)
      return "MQ_ERROR_QUEUE_NOT_FOUND";
    if (hr == -1072824315)
      return "MQ_ERROR_QUEUE_EXISTS";
    if (hr == -1072824314)
      return "MQ_ERROR_INVALID_PARAMETER";
    if (hr == -1072824313)
      return "MQ_ERROR_INVALID_HANDLE";
    if (hr == -1072824230)
      return "MQ_ERROR_QUEUE_DELETED";
    if (hr == -1072824309)
      return "MQ_ERROR_SERVICE_NOT_AVAILABLE";
    if (hr == -1072824293)
      return "MQ_ERROR_IO_TIMEOUT";
    if (hr == -1072824290)
      return "MQ_ERROR_ILLEGAL_FORMATNAME";
    if (hr == -1072824283)
      return "MQ_ERROR_ACCESS_DENIED";
    if (hr == -1072824301)
      return "MQ_ERROR_NO_DS";
    if (hr == -1072824257)
      return "MQ_ERROR_INSUFFICIENT_PROPERTIES";
    if (hr == -1072824300)
      return "MQ_ERROR_ILLEGAL_QUEUE_PATHNAME";
    if (hr == -1072824252)
      return "MQ_ERROR_INVALID_OWNER";
    if (hr == -1072824251)
      return "MQ_ERROR_UNSUPPORTED_ACCESS_MODE";
    if (hr == -1072824215) {
      return "MQ_ERROR_REMOTE_MACHINE_NOT_AVAILABLE";
    }
    return "unknown hr (" + hr + ")";
  }
}