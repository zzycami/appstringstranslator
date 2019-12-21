<template>
    <div class="upload-container" >
        <el-upload  class="upload" :on-success="onSuccess" drag action="http://localhost/upload" multiple>
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
</template>

<script>
    import axios from 'axios';

    export default {
        name: "Upload",
        data() {
            return {
                fileList: [],
            }
        },
        methods: {
            onSuccess(response) {
                window.console.log(response);
                this.translatedList = response;
            },
            handleClick(row) {
                window.console.log(row);
                let self = this;
                axios.get("http://localhost/strings/" + row.id).then(function (response) {
                    self.translatedList = response.data;
                });
            }
        },
        components: {
        },
        computed: {
            showUpload(){
                return this.translatedList.length === 0;
            }
        },
        created: function () {
            let self = this;
            axios.get("http://localhost/files").then(function (response) {
                window.console.log(response);
                self.fileList = response.data;
            })
        }
    }
</script>

<style scoped>
    .upload-container {
        text-align: center;
    }
    .file-list {
        margin-top: 20px;
    }
</style>