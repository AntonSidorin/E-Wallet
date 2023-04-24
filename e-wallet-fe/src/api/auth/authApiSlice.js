import { apiSlice } from 'api/apiSlice';

export const authApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    login: builder.mutation({
      query: credentials => ({
        url: '/api/v1/auth/authenticate',
        method: 'POST',
        body: { ...credentials }
      }),
    }),
    register: builder.mutation({
      query: credentials => ({
        url: '/api/v1/auth/register',
        method: 'POST',
        body: { ...credentials }
      }),
    }),
  }),
});

export const {
  useLoginMutation,
  useRegisterMutation,
} = authApiSlice;