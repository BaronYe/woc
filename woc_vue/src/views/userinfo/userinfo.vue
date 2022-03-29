<template>
    <div class="container">
      <div style="margin-left: auto;margin-right: 10px;position: relative;top: 2px">

      </div>
        <el-form ref="form" :model="admin" v-if="admin != null" label-width="80px">
            <el-form-item label="头像">
                <el-col :span="10">
                    <el-upload
                            class="avatar-uploader"
                            :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess">
                        <img v-if="admin" :src="admin.avatar" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-col>
            </el-form-item>
            <el-form-item label="昵称">
                <el-col :span="10">
                    <el-input v-model="admin.name"></el-input>
                </el-col>
            </el-form-item>
            <el-form-item label="账号">
                <el-col :span="10">
                    <el-input v-model="admin.account" disabled></el-input>
                </el-col>
            </el-form-item>
            <el-form-item label="创建时间">
                <el-col :span="10">
                    <el-input v-model="admin.createTime" disabled></el-input>
                </el-col>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit">保存</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: 'index',
        data(){
            return{
                admin:null,
            }
        },
        mounted() {
            this.admin = JSON.parse(sessionStorage.getItem('admin'));
        },
        methods:{
            handleAvatarSuccess(res) {
                this.admin.avatar = res.data;
            },
            // 保存
            onSubmit(){
                this.api.setAdmin({
                    id:this.admin.id,
                    avatar:this.admin.avatar,
                    name:this.admin.name,
                }).then(res=>{
                    sessionStorage.setItem('admin',JSON.stringify(this.admin));
                    this.$message.success("修改成功")
                })

            } ,

        }
    };
</script>

<style scoped lang="less">
    /deep/ .el-upload--text{
        width: 100%;
        height: 100%;
        border:none;
    }
    .avatar-uploader{
        width: 80px;
        height: 80px;
    }
    .avatar {
        width: 80px;
        height: 80px;
        display: block;
    }
</style>
