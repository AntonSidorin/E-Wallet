import { Routes, Route } from 'react-router-dom'
import Layout from 'component/util/Layout'
import Welcome from 'component/Welcome'
import RequireAuth from 'component/auth/RequireAuth'
import Register from 'component/auth/Register';
import Dashboard from 'component/dashboard/Dashboard';
import Wallet from 'component/wallet/Wallet';
import CreateWallet from 'component/wallet/action/CreateWallet';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        {/* public routes */}
        <Route index element={<Welcome />} />
        <Route path="register" element={<Register />} />

        {/* protected routes */}
        <Route element={<RequireAuth />}>
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="createWallet" element={<CreateWallet />} />
          <Route path="wallet/:walletId" element={<Wallet />} />
        </Route>

      </Route>
    </Routes>
  )
}

export default App;
