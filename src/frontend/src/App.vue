<template>
  <img alt="logo" src="./assets/logo.png">
  <UserAccount msg="Welcome to Your Vue.js App"/>
</template>

<script>
import UserAccount from './components/UserAccount.vue'

export default {
  name: 'App',
  components: {
    UserAccount
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin: 5% 10%;
}
</style>
