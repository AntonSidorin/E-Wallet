const RefreshDashboardWidget = ({refetch}) => {
  return (
    <div onClick={refetch} className="walletWidget walletActionWidget">
      <span>Refresh</span>
    </div>
  );
};

export default RefreshDashboardWidget;