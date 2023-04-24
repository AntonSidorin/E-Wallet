import CollapsibleAction from 'component/wallet/action/CollapsibleAction';

const Transaction = ({transaction}) => {

  return (
    <CollapsibleAction key={transaction.id} label={`${transaction.transactionDate} - ${transaction.description} - ${transaction.amount}`}>
      <div>
        {transaction.amount ? <div>Amount: {transaction.amount}</div> : null}
        {transaction.balance ? <div>Balance: {transaction.balance}</div> : null}
        {transaction.transactionDate ? <div>Transaction date: {transaction.transactionDate}</div> : null}
      </div>
    </CollapsibleAction>
  );
};

export default Transaction;