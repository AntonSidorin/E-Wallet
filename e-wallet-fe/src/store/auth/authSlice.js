import { createSlice } from '@reduxjs/toolkit';
import jwt from 'jwt-decode';

const authSlice = createSlice({
  name: 'auth',
  initialState: { user: null, token: null },
  reducers: {
    setCredentials: (state, action) => {
      const { token } = action.payload;
      state.user = jwt(token);
      state.token = token;
    },
    logOut: (state) => {
      state.user = null
      state.token = null
    }
  },
});

export const {setCredentials, logOut} = authSlice.actions

export default authSlice.reducer

export const selectCurrentUser = (state) => state.auth.user
export const selectCurrentToken = (state) => state.auth.token