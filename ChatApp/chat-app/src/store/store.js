import { createStore } from 'vuex';

export default createStore({
    state: {
        username: localStorage.getItem('username') || null,
        userId: localStorage.getItem('userId') || null
    },
    mutations: {
        setUsername(state, username) {
            state.username = username;
            localStorage.setItem('username', username);
        },
        setUserId(state, userId) {
            state.userId = userId;
            localStorage.setItem('userId', userId);
        },
        clearUsername(state) {
            state.username = null;
            state.userId = null;
            localStorage.removeItem('username');
            localStorage.removeItem('userId');
        }
    },
    actions: {
        updateUsername({ commit }, username) {
            commit('setUsername', username);
        },
        updateUserId({ commit }, userId) {
            commit('setUserId', userId);
        },
        logoutUser({ commit }) {
            commit('clearUsername');
        }
    },
    getters: {
        getUsername: (state) => state.username,
        getUserId: (state) => state.userId
    }
});
