import { createStore } from 'vuex';

export default createStore({
    state: {
        username: null
    },
    mutations: {
        setUsername(state, username) {
            state.username = username;
        }
    },
    actions: {
        updateUsername({ commit }, username) {
            commit('setUsername', username);
        }
    },
    getters: {
        getUsername: (state) => state.username
    }
});
