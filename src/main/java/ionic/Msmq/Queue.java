package ionic.Msmq;

import java.io.UnsupportedEncodingException;

public class Queue
{
  int _queueSlot = 0;
  String _name;
  String _formatName;
  String _label;
  boolean _isTransactional;

  public Queue(String queueName)
    throws MessageQueueException
  {
    _init(queueName, 3);
  }

  public Queue(String queueName, int access)
    throws MessageQueueException
  {
    _init(queueName, access);
  }

  private void _init(String queueName, int access)
    throws MessageQueueException
  {
    int rc = 0;
    if (access == 1)
    {
      rc = nativeOpenQueueForReceive(queueName);
    }
    else if (access == 2)
    {
      rc = nativeOpenQueueForSend(queueName);
    }
    else if (access == 3)
    {
      rc = nativeOpenQueue(queueName);
    }
    else rc = -1072824314;

    if (rc != 0) throw new MessageQueueException("Cannot open queue.", rc);

    this._name = queueName;
    this._formatName = "unknown";
    this._label = "need to set this";
    this._isTransactional = false;
  }

  public static Queue create(String queuePath, String queueLabel, boolean isTransactional)
    throws MessageQueueException
  {
    int rc = nativeCreateQueue(queuePath, queueLabel, isTransactional ? 1 : 0);
    if (rc != 0) {
      throw new MessageQueueException("Cannot create queue.", rc);
    }
    String a1 = "OS";
    char[] c = queuePath.toCharArray();
    if ((c[0] >= '1') && (c[0] <= '9')) {
      a1 = "TCP";
    }
    Queue q = new Queue("DIRECT=" + a1 + ":" + queuePath);
    q._name = queuePath;

    q._label = queueLabel;
    q._isTransactional = isTransactional;
    return q;
  }

  public static void delete(String queuePath)
    throws MessageQueueException
  {
    int rc = nativeDeleteQueue(queuePath);
    if (rc != 0)
      throw new MessageQueueException("Cannot delete queue.", rc);
  }

  public void send(Message msg, boolean highPriority, TransactionType t) throws MessageQueueException {
    int rc = nativeSendBytes(msg.getBody(), msg.getLabel(), msg.getCorrelationId(), t.getValue(), highPriority);
    this.close();
    if (rc != 0)
      throw new MessageQueueException("Cannot send.", rc);
  }

  public void send(Message msg, TransactionType t)
    throws MessageQueueException
  {
    send(msg, false, t);
  }

  public void send(Message msg, boolean highPriority)
    throws MessageQueueException
  {
    send(msg, highPriority, TransactionType.None);
  }

  public void send(Message msg)
    throws MessageQueueException
  {
    send(msg, false, TransactionType.None);
  }

  public void send(String s)
    throws MessageQueueException, UnsupportedEncodingException
  {
    int rc = nativeSendBytes(s.getBytes("UTF-8"), "", null, 0, false);

    if (rc != 0)
      throw new MessageQueueException("Cannot send.", rc);
  }

  public void send(byte[] b)
    throws MessageQueueException
  {
    int rc = nativeSendBytes(b, "", null, 0, false);

    if (rc != 0)
      throw new MessageQueueException("Cannot send.", rc);
  }

  private Message _internal_receive(int timeout, int ReadOrPeek) throws MessageQueueException {
    Message msg = new Message();
    int rc = nativeReceiveBytes(msg, timeout, ReadOrPeek);
    this.close();
    if (rc != 0) {
      throw new MessageQueueException("Cannot receive.", rc);
    }
    return msg;
  }

  public Message receive(int timeout)
    throws MessageQueueException
  {
    return _internal_receive(timeout, 1);
  }

  public Message receive()
    throws MessageQueueException
  {
    return _internal_receive(0, 1);
  }

  public Message peek()
    throws MessageQueueException
  {
    return _internal_receive(0, 0);
  }

  public Message peek(int timeout)
    throws MessageQueueException
  {
    return _internal_receive(timeout, 0);
  }

  public void close()
    throws MessageQueueException
  {
    int rc = nativeClose();
    if (rc != 0)
      throw new MessageQueueException("Cannot close.", rc);
  }

  public String getName()
  {
    return this._name;
  }

  public String getLabel()
  {
    return this._label;
  }

  public String getFormatName()
  {
    return this._formatName;
  }

  public boolean isTransactional()
  {
    return this._isTransactional;
  }

  private static native int nativeInit();

  private static native int nativeCreateQueue(String paramString1, String paramString2, int paramInt);

  private static native int nativeDeleteQueue(String paramString);

  private native int nativeOpenQueue(String paramString);

  private native int nativeOpenQueueForSend(String paramString);

  private native int nativeOpenQueueForReceive(String paramString);

  private native int nativeSend(String paramString1, int paramInt1, String paramString2, String paramString3, int paramInt2);

  private native int nativeReceiveBytes(Message paramMessage, int paramInt1, int paramInt2);

  private native int nativeSendBytes(byte[] paramArrayOfByte1, String paramString, byte[] paramArrayOfByte2, int paramInt, boolean paramBoolean);

  private native int nativeClose();

  static
  {
    System.loadLibrary("MsmqJava64");
    nativeInit();
  }
}