<template>
  <div class="upload-container">
    <el-upload  class="upload" :on-success="onSuccess" drag :action="uploadUrl" multiple>
      <i class="el-icon-upload"/>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip" slot="tip">只能上传strings文件，且不超过500kb</div>
    </el-upload>
    <el-table class="file-list" :data="fileList" border>
      <el-table-column fixed prop="filename" label="文件名"/>
      <el-table-column fixed prop="size" label="大小"/>
      <el-table-column fixed prop="createTime" label="上传时间"/>
      <el-table-column fixed="right" label="操作" width="100">
        <template slot-scope="scope">
          <el-button @click="handleCheckFile(scope.row)" type="text" size="small">查看</el-button>
          <el-button @click="handleDeleteFile(scope.row)" type="text" size="small">删除</el-button>
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
      uploadUrl: ""
    }
  },
  methods: {
    onSuccess(response) {
      console.log(response);
      this.stringsList = response;
    },
    handleDeleteFile(row) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete("files/" + row.id).then((response)=> {
          this.loadFiles();
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
    },
    handleCheckFile(row) {
      this.$router.push(`/file/${row.id}`);
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
  .upload-container {
    text-align: center;
  }
</style>

