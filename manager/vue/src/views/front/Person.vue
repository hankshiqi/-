<template>
  <div class="main-content">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="个人资料" name="个人资料">
        <person-page @update:user="updateUser"/>
      </el-tab-pane>
      <el-tab-pane label="我发表的博客" name="我发表的博客">
        <div class="card main-content" style="margin-bottom: 10px;">
          <el-button type="primary" @click="makeNewBlog">发布新博客</el-button>
        </div>
        <div class="main-content">
          <blog-list type="user" :showEdit="true"/>
        </div>
      </el-tab-pane>
      <el-tab-pane label="我报名的活动" name="我报名的活动">
        <activity-list type="user" :span="8"/>
      </el-tab-pane>
      <el-tab-pane label="我的收藏" name="我的收藏">
        <div class="main-content" >
          <div class="card" style="display: flex;align-items: center;margin-bottom: 10px;">
            <div class="collectItem" :class="{active: collectType === 'blog'}" @click="() => {collectType = 'blog'}">博客</div>
            <div class="collectItem" :class="{active: collectType === 'activity'}" @click="() => {collectType = 'activity'}">活动</div>
          </div>
          <div>
            <blog-list type="collect" :showEdit="false" v-if="collectType === 'blog'"/>
            <div v-if="collectType === 'activity'" style="width: 100%;">
              <activity-list type="collect" :span="8" />
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
   
  </div>
</template>

<script>
import PersonPage from '@/components/PersonPage.vue'
import BlogList from '@/components/BlogList.vue'
import ActivityList from '@/components/ActivityList.vue'
export default {
  components: { PersonPage, BlogList, ActivityList },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      dialogVisible: false,
      activeName:'个人资料',
      rules: {
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      },
      collectType: 'blog'
    }
  },
  created() {

  },
  methods: {
    updateUser() {
      this.$emit('update:user')
    },
    handleClick(tab, event) {
        console.log(tab, event);
    },
    update() {
      // 保存当前的用户信息到数据库
      this.$request.put('/admin/update', this.user).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.$message.success('保存成功')
          // 更新浏览器缓存里的用户信息
          localStorage.setItem('xm-user', JSON.stringify(this.user))

          // 触发父级的数据更新
          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把user的头像属性换成上传的图片的链接
      this.$set(this.user, 'avatar', response.data)
    },
    // 修改密码
    updatePassword() {
      this.dialogVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request.put('/updatePassword', this.user).then(res => {
            if (res.code === '200') {
              // 成功更新
              this.$message.success('修改密码成功')
              this.$router.push('/login')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    makeNewBlog(){
      window.open('/front/newBlog')
    },

  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
}
/deep/.el-upload {
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
}
.collectItem{
  width: fit-content;
  height: 30px;
  border-radius: 3px;
  background-color: #409EFF;
  color: #fff;
  text-align: center;
  line-height: 30px;
  margin-left:10px;
  cursor: pointer;
}
.active{
  background-color: #061f42;
  color: #ffffff;
}
</style>