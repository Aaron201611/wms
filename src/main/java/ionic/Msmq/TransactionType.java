package ionic.Msmq;

public enum TransactionType
{
  None(0), 

  MTS(1), 

  XA(2), 

  SINGLE_MESSAGE(3);

  int _transactionFlag;

  private TransactionType(int value)
  {
    this._transactionFlag = value;
  }
  public int getValue() {
    return this._transactionFlag;
  }
}