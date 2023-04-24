import Loading from 'component/util/Loading';
import Transaction from 'component/wallet/transaction/Transaction';
import { useGetWalletTransactionsQuery } from 'api/wallet/walletApiSlice';
import RefreshDashboardWidget from '../widget/RefreshDashboardWidget';
import Error from '../../error/Error';

const WalletTransactions = ({ walletId }) => {

  const {
    data: transactions,
    refetch,
    isLoading,
    isError,
  } = useGetWalletTransactionsQuery(walletId);

  if (isLoading) return <Loading />;

  if (isError) return (
    <>
      <RefreshDashboardWidget refetch={refetch} />
      <Error message="Please try to refresh." />
    </>
  );

  return (
    <>
      <RefreshDashboardWidget refetch={refetch} />
      {transactions?.length > 0 ?
        <div>
          {transactions.map((transaction) => {
            return <Transaction key={transaction.id} transaction={transaction} />;
          })}
        </div> :
        <p>The wallet doesn't have any transactions.</p>
      }
    </>
  )

}

export default WalletTransactions;