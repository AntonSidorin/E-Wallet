import { apiSlice } from 'api/apiSlice';

export const walletApiSlice = apiSlice.injectEndpoints({
  tagTypes: ['Wallet', 'Transaction'],
  keepUnusedDataFor: 30,
  endpoints: builder => ({
    createWallet: builder.mutation({
      query: wallet => ({
        url: '/api/v1/wallets',
        method: 'POST',
        body: { ...wallet },
      }),
      invalidatesTags: ['Wallet'],
    }),
    topupWallet: builder.mutation({
      query: ({ walletId, amount }) => ({
        url: `/api/v1/wallets/${walletId}/topup/${amount}`,
        method: 'PATCH',
      }),
      async onQueryStarted({ walletId, ...patch }, { dispatch, queryFulfilled }) {
        try {
          const { data: updatedWallet } = await queryFulfilled
          dispatch(
            walletApiSlice.util.updateQueryData('getWallet', walletId, (draft) => {
              Object.assign(draft, updatedWallet)
            })
          )
          dispatch(walletApiSlice.util.invalidateTags(['Transaction']))
        } catch { }
      },
    }),
    withdrawWallet: builder.mutation({
      query: ({ walletId, amount }) => ({
        url: `/api/v1/wallets/${walletId}/withdraw/${amount}`,
        method: 'PATCH',
      }),
      async onQueryStarted({ walletId, ...patch }, { dispatch, queryFulfilled }) {
        try {
          const { data: updatedWallet } = await queryFulfilled
          dispatch(
            walletApiSlice.util.updateQueryData('getWallet', walletId, (draft) => {
              Object.assign(draft, updatedWallet)
            })
          )
          dispatch(walletApiSlice.util.invalidateTags(['Transaction']))
        } catch { }
      },
    }),
    transfer: builder.mutation({
      query: ({ walletId, transferWalletId, amount }) => ({
        url: `/api/v1/wallets/${walletId}/transfer/${transferWalletId}/${amount}`,
        method: 'PATCH',
      }),
      async onQueryStarted({ walletId, ...patch }, { dispatch, queryFulfilled }) {
        try {
          const { data: updatedWallet } = await queryFulfilled
          dispatch(
            walletApiSlice.util.updateQueryData('getWallet', walletId, (draft) => {
              Object.assign(draft, updatedWallet)
            })
          )
          dispatch(walletApiSlice.util.invalidateTags(['Transaction']))
        } catch { }
      },
    }),
    getWallets: builder.query({
      query: () => '/api/v1/wallets',
      keepUnusedDataFor: 0,
      providesTags: (result) =>
        result
          ? [...result.map(({ id }) => ({ type: 'Wallet', id })), 'Wallet']
          : ['Wallet'],
      invalidatesTags: ['Transaction'],
    }),
    getWallet: builder.query({
      query: id => `/api/v1/wallets/${id}`,
      providesTags: (result) => [{ type: 'Wallet', id: result.id }],
      invalidatesTags: ['Transaction'],
    }),
    getWalletTransactions: builder.query({
      query: walletId => `/api/v1/wallets/${walletId}/transactions`,
      providesTags: ['Transaction']
    }),
  }),
});

export const {
  useCreateWalletMutation,
  useTopupWalletMutation,
  useWithdrawWalletMutation,
  useTransferMutation,
  useGetWalletsQuery,
  useGetWalletQuery,
  useGetWalletTransactionsQuery,
} = walletApiSlice;