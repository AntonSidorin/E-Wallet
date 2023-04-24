const WalletInformation = ({wallet}) => {

  return (
    <div className="wallet">
      <div className="wallet-name">
        <h1>{wallet.name}</h1>
      </div>
      <div className="wallet-balance">
        <h1>{(Math.round(wallet.balance * 100) / 100).toFixed(2)}</h1>
      </div>
      {wallet.description? <p>{wallet.description}</p>: null}
    </div>
  )

};

export default WalletInformation;