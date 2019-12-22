<template>
  <div>
    <header class="header">
      <nav class="inner" role="navigation">
        <router-link to="/" exact>
          <img class="logo" src="~/assets/logo.svg" alt="logo">文件列表
        </router-link>

        <div class="github">
          <a href="https://github.com/zzycami/appstringstranslator">Fork me on GitHub</a>
        </div>

      </nav>
    </header>
    <nuxt id="app" />
  </div>
</template>

<script>
export default {
  methods: {
    handleSelect() {

    }
  }
}
</script>

<style >
.header {
  background-color: #2E495E;
  z-index: 999;
  height: 55px;
}

.header .inner {
  width: 90%;
  box-sizing: border-box;
  margin: 0px auto;
  padding: 15px 5px;
}

.header a {
  color: #fff;
  line-height: 24px;
  text-decoration: none;
  transition: color 0.15s ease;
  display: inline-block;
  vertical-align: middle;
  font-weight: 300;
  letter-spacing: 0.075em;
  margin-right: 1.8em;
}

.github {
  color: #fff;
  font-size: 0.9em;
  margin: 0;
  float: right;

  background-color: #a00;
  overflow: hidden;
  white-space: nowrap;
  /* top right corner */
  position: fixed;
  right: -50px;
  top: 40px;
  /* 45 deg ccw rotation */
  -webkit-transform: rotate(45deg);
  -moz-transform: rotate(45deg);
  -ms-transform: rotate(45deg);
  -o-transform: rotate(45deg);
  transform: rotate(45deg);
  /* shadow */
  -webkit-box-shadow: 0 0 10px #888;
  -moz-box-shadow: 0 0 10px #888;
  box-shadow: 0 0 10px #888;
}

.github a {
  border: 1px solid #faa;
  color: #fff;
  display: block;
  font: bold 81.25% 'Helvetica Neue', Helvetica, Arial, sans-serif;
  margin: 1px 0;
  padding: 10px 50px;
  text-align: center;
  text-decoration: none;
  /* shadow */
  text-shadow: 0 0 5px #444;
}

.logo {
  width: 24px;
  margin-right: 10px;
  display: inline-block;
  vertical-align: middle;
}

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin: 80px auto;
  width: 90%;
}

.header {
  position: fixed;
  z-index:10000;
  top:0;
  width:100%;
}

html {
  font-family: 'Source Sans Pro', -apple-system, BlinkMacSystemFont, 'Segoe UI',
    Roboto, 'Helvetica Neue', Arial, sans-serif;
  font-size: 16px;
  word-spacing: 1px;
  -ms-text-size-adjust: 100%;
  -webkit-text-size-adjust: 100%;
  -moz-osx-font-smoothing: grayscale;
  -webkit-font-smoothing: antialiased;
  box-sizing: border-box;
}

*,
*:before,
*:after {
  box-sizing: border-box;
  margin: 0;
}

.button--green {
  display: inline-block;
  border-radius: 4px;
  border: 1px solid #3b8070;
  color: #3b8070;
  text-decoration: none;
  padding: 10px 30px;
}

.button--green:hover {
  color: #fff;
  background-color: #3b8070;
}

.button--grey {
  display: inline-block;
  border-radius: 4px;
  border: 1px solid #35495e;
  color: #35495e;
  text-decoration: none;
  padding: 10px 30px;
  margin-left: 15px;
}

.button--grey:hover {
  color: #fff;
  background-color: #35495e;
}
</style>
