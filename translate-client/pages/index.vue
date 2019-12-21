<template>
  <div id="app">
    <div class="upload-container" v-if="showUpload">
      <el-upload  class="upload" :on-success="onSuccess" drag :action="uploadUrl" multiple>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传strings文件，且不超过500kb</div>
      </el-upload>
      <el-table class="file-list" :data="fileList" border>
        <el-table-column fixed prop="filename" label="文件名" ></el-table-column>
        <el-table-column fixed prop="size" label="大小" ></el-table-column>
        <el-table-column fixed prop="createTime" label="上传时间" ></el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
            <el-button type="text" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div v-else>
      <div class="operation-panel">
        <el-button>全部选择</el-button>
        <el-button>翻译全部</el-button>
        <el-dropdown class="language-list" split-button type="primary" @command="handleSelectLanguage">
          翻译语言
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item :key="language.value" :command="language" v-for="language in languageList">{{ language.name + " - " + language.native }}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-dropdown class="language-list" split-button type="primary" @command="handleSelectTranslateLanguage">
          下载翻译文件
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item :key="language.value" :command="language" v-for="language in translatedList">{{ language.name + " - " + language.native }}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-progress v-show="translating" class="progress" type="line" :percentage="progress"></el-progress>
      </div>
      <el-table v-loading="translating"
                element-loading-text="拼命加载中"
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)"
                :data="stringsList" border>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column fixed prop="key" label="名称" ></el-table-column>
        <el-table-column fixed prop="origin" label="原版翻译" ></el-table-column>
        <el-table-column :key="translated.value" v-for="translated in translatedList" fixed :prop="translated.value" :label="translated.name" ></el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
            <el-button type="text" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  import config from '~/nuxt.config';
  import translate from 'google-translate-open-api';


  async function test() {
    const result = await translate(`I'm fine.`, {
      tld: "cn",
      to: "zh-CN",
    });
    const data = result.data[0];
    console.log(data);
  }

  test();

  function sleep (time) {
    return new Promise((resolve) => setTimeout(resolve, time));
  }

  export default {
    name: 'app',
    data() {
      return {
        stringsList: [],
        fileList: [],
        languageList: [],
        translatedList: [],
        currentFile: null,
        translating: false,
        progress: 0,
        uploadUrl: ""
      }
    },
    methods: {
      onSuccess(response) {
        console.log(response);
        this.stringsList = response;
      },
      handleClick(row) {
        console.log(row);
        let self = this;
        this.currentFile = row;
        let translatedList = row.translatedLanguage.split(",");
        let languageList = [];
        this.languageList.forEach((item)=> {
          if(translatedList.includes(item.value)) {
            this.translatedList.push(item);
          } else {
            languageList.push(item);
          }
        });
        //this.languageList = languageList;
        this.$axios.get("strings/" + row.id).then(function (response) {
          self.stringsList = response.data;
        });
      },
      loadLanguages: function () {
        let self = this;
        this.$axios.get("language").then(function (response) {
          console.log(response);
          self.languageList = response.data;
        })
      },
      loadFiles: function () {
        let self = this;
        this.$axios.get("files").then(function (response) {
          console.log(response);
          self.fileList = response.data;
        })
      },
      handleSelectTranslateLanguage: function(command) {
        // axios.get(`translate_file/file/${this.currentFile.id}/key/${command.value}`);
        window.open(`http://translate.bravedefault.com/translate_file/file/${this.currentFile.id}/key/${command.value}`, "_blank");
      },
      handleSelectLanguage: async function(command) {
        this.translating = true;
        for(let index = 0; index < this.stringsList.length; index++) {
          let string = this.stringsList[index];
          this.translateSingle(index, string.id, 'zh', command.value);
          this.progress = parseInt((index / this.stringsList.length) * 100);
          await sleep(1500);
        }
        this.translating = false;
        // axios.get("strings/" + this.currentFile.id).then(function (response) {
        //   self.stringsList = response.data;
        // });
        if(!this.translatedList.includes(command)) {
          this.translatedList.push(command);
        }
      },
      translateSingle(index, id, from, to) {
        let self = this;
        this.$axios.get(`translate/strings/${id}/from/${from}/to/${to}`).then(function (response) {
          //window.console.log(response);
          let result = response.data.trans_result[0].dst;
          self.stringsList[index][to] = result;
          self.$forceUpdate();
          window.console.log(self.stringsList);
        })
      }
    },
    components: {
    },
    computed: {
      showUpload(){
        return this.stringsList.length === 0;
      }
    },
    created: function () {
      this.uploadUrl = config.axios.baseURL + "/upload";
      this.loadFiles();
      this.loadLanguages();
    }
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
    margin: 60px auto;
    width: 80%;
  }
  .operation-panel {
    margin-bottom: 30px;
  }
  .upload-container {
    text-align: center;
  }
  .file-list {
    margin-top: 20px;
  }
  .language-list {
    margin-left: 10px;
  }
  .progress {
    margin-top: 20px;
  }
</style>
