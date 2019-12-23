<template>
  <div id="app">
    <el-page-header class="page-header" @back="goBack" title="返回" :content="currentFile ? currentFile.filename : ''" />
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
      <el-progress v-show="translating" class="progress" type="line" :percentage="progress"/>
    </div>
    <el-table v-loading="translating"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              :data="stringsList" border>
      <el-table-column type="selection" width="55"/>
      <el-table-column fixed prop="key" label="名称" width="150"/>
      <el-table-column prop="origin" label="原版翻译" :width="translatedList.length > 3 ? 150 : ''"/>
      <el-table-column v-if="translatedList.length > 0" :key="translated.value" width="150" v-for="translated in translatedList" :prop="translated.value"
                       :label="translated.name"/>
      <el-table-column fixed="right" label="操作" width="120">
        <template slot-scope="scope">
          <el-dropdown class="language-list"  @command="handleSingleSelectTranslateLanguage">
            <el-button type="text" size="small" class="el-dropdown-link">
              翻译<i class="el-icon-arrow-down el-icon--right"/>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :key="language.value" :command="{language, scope: scope}" v-for="language in translatedList">{{ language.name + " - " + language.native }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-button @click="handleDeleteSingle(scope.row)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import config from '~/nuxt.config';
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
    goBack() {
      this.$router.back();
    },
    loadStrings(fileId) {
      return new Promise(((resolve, reject) => {
        this.$axios.get(`strings/${fileId}`).then(response => {
          resolve(response.data);
        }).catch(error => {
          reject(error);
        });
      }));
    },
    loadLanguages: function () {
      return new Promise((resolve, reject)=> {
        this.$axios.get("language").then(response => {
          console.log(response);
          resolve(response.data);
        }).catch((error)=> {
          reject(error)
        })
      });
    },
    loadFile: function (id) {
      return new Promise((resolve, reject) => {
        this.$axios.get(`files/${id}`).then( (response)=> {
          console.log(response);
          resolve(response.data);
        }).catch((error)=> {
          reject(error);
        })
      });
    },
    handleSelectTranslateLanguage: function(command) {
      window.open(`${config.axios.baseURL}/translate_file/file/${this.currentFile.id}/key/${command.value}`, "_blank");
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
      if(!this.translatedList.includes(command)) {
        this.translatedList.push(command);
      }
    },
    handleSingleSelectTranslateLanguage(command) {
      console.log(command);
      let id = command.scope.row.id;
      let from = command.scope.row.origin;
      let to = command.language.value;
      let index = command.scope.$index;
      this.translating = true;
      this.$axios.get(`translate/strings/${id}/from/${from}/to/${to}`).then(response => {
        let result = response.data.trans_result[0].dst;
        this.stringsList[index][to] = result;
        this.translating = false;
      })
    },
    translateSingle(index, id, from, to) {
      this.$axios.get(`translate/strings/${id}/from/${from}/to/${to}`).then(response => {
        let result = response.data.trans_result[0].dst;
        this.stringsList[index][to] = result;
      })
    },
    handleDeleteSingle(row) {
      this.$confirm('此操作将永久删除该条记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`strings/${row.id}`).then((response)=> {
          this.loadStrings(this.currentFile.id).then(response => {
            this.stringsList = response;
          }).catch(error => {
            this.$message({
              type: 'error',
              message: error.message
            });
          });
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
        }).catch((error) => {
          this.$message({
            type: 'error',
            message: '删除失败!'
          });
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    }
  },
  components: {
  },
  computed: {
    fileId() {
      return this.$route.params.id
    },
    showUpload(){
      return this.stringsList.length === 0;
    },
  },
  created: function () {
    this.loadLanguages().then(languages => {
      this.languageList = languages;
      this.loadFile(this.fileId).then( file =>{
        this.currentFile = file;
        if (file.translatedLanguage) {
          console.log(file.translatedLanguage);
          let translatedList = file.translatedLanguage.split(",");
          console.log(translatedList);
          this.translatedList = [];
          this.languageList.forEach((item)=> {
            if(translatedList.includes(item.value)) {
              this.translatedList.push(item);
            }
          });
          console.log(this.translatedList);
        }
        this.loadStrings(file.id).then(response => {
          this.stringsList = response;
        }).catch(error => {
          this.$message({
            type: 'error',
            message: error.message
          });
        });
      }).catch( error => {
        this.$message({
          type: 'error',
          message: error.message
        });
      });
    }).catch(error => {
      this.$message({
        type: 'error',
        message: error.message
      });
    });
  }
}
</script>

<style>
  .operation-panel {
    margin-bottom: 30px;
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
  .page-header {
    margin-bottom: 30px;
  }
</style>
