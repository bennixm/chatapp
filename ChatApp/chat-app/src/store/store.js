import { createStore } from 'vuex';

export default createStore({
    state: {
        username: localStorage.getItem('username') || null
    },
    mutations: {
        setUsername(state, username) {
            state.username = username;
            localStorage.setItem('username', username);
        },
        clearUsername(state) {
            state.username = null;
            localStorage.removeItem('username');
        }
    },
    actions: {
        updateUsername({ commit }, username) {
            commit('setUsername', username);
        },
        logoutUser({ commit }) {
            commit('clearUsername');
        }
    },
    getters: {
        getUsername: (state) => state.username
    }
});
