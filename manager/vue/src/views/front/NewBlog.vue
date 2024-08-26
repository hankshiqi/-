<!-- NewBlog.vue -->
<template>
    <div>
        <div class="card main-content">
            <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
                <el-form-item prop="title" label="标题">
                    <el-input v-model="form.title" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item prop="descr" label="简介">
                    <el-input v-model="form.descr" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item prop="cover" label="封面">
                    <el-upload :action="$baseUrl + '/files/upload'" :headers="{ token: user.token }" list-type="picture"
                        :on-success="handleImgSuccess">
                        <el-button type="primary">上传</el-button>
                    </el-upload>
                </el-form-item>
                <el-form-item label="标签" prop="tags">
                    <el-select v-model="tagsArr" multiple filterable allow-create default-first-option
                        style="width: 100%">
                        <el-option value="后端"></el-option>
                        <el-option value="Java"></el-option>
                        <el-option value="面试"></el-option>
                        <el-option value="Vue"></el-option>
                        <el-option value="前端"></el-option>
                        <el-option value="大数据"></el-option>
                        <el-option value="算法"></el-option>
                        <el-option value="程序员"></el-option>
                        <el-option value="小白"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="categoryName" label="分类">
                    <el-select v-model="form.categoryName" filterable style="width: 100%">
                        <el-option v-for="item in categoryList" :key="item.id" :label="item.name"
                            :value="item.name"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                    <div id="editor"></div>
                </el-form-item>
            </el-form>
            <div style="text-align: center;">
                <el-button type="primary" @click="save" size="large">发布</el-button>
            </div>
        </div>
    </div>
</template>

<script>
import E from "wangeditor"
import hljs from 'highlight.js'
import { set } from 'vue';
export default {
    name: 'NewBlog',
    data() {
        return {
            tableData: [],  // 所有的数据
            pageNum: 1,   // 当前的页码
            pageSize: 10,  // 每页显示的个数
            total: 0,
            title: null,
            userName: null,
            categoryName: null,
            fromVisible: false,
            form: {},
            content: '',
            user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
            rules: {
                title: [
                    { required: true, message: '请输入标题', trigger: 'blur' },
                ]
            },
            ids: [],
            tagsArr: [],
            categoryList: [],
            editor: null,
            blogId: this.$route.query.blogId
        };
    },

    components: {},

    computed: {},

    mounted() {
        this.$request.get('/category/selectAll').then(res => {
            this.categoryList = res.data || []
        })
        this.$request.get('/blog/selectById/'+this.blogId).then(res => {
            this.form = res.data||{}
            this.content = this.form.content
            this.tagsArr = JSON.parse(this.form.tags||'[]')
            setTimeout(() => {
                this.editor.txt.html(this.content)
            }, 0)
        })
        this.setRichText()
    },

    methods: {
        handleImgSuccess(response, file, fileList) {
            this.form.cover = response.data
        },
        setRichText() {
            this.$nextTick(() => {
                this.editor = new E(`#editor`)
                this.editor.highlight = hljs
                this.editor.config.uploadImgServer = this.$baseUrl + '/files/editor/upload'
                this.editor.config.uploadFileName = 'file'
                this.editor.config.zIndex = 0
                this.editor.config.uploadImgHeaders = {
                    token: this.user.token
                }
                this.editor.config.uploadImgParams = {
                    type: 'img',
                }
                this.editor.create()  // 创建
            })
        },
        save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
            this.$refs.formRef.validate((valid) => {
                if (valid) {
                    this.form.tags = JSON.stringify(this.tagsArr)
                    this.form.userName = this.user.name
                    this.form.content = this.editor.txt.html()
                    this.$request({
                        url: this.form.id ? '/blog/update' : '/blog/add',
                        method: this.form.id ? 'PUT' : 'POST',
                        data: this.form
                    }).then(res => {
                        if (res.code === '200') {  // 表示成功保存
                            this.$message.success('保存成功')
                        } else {
                            this.$message.error(res.msg)  // 弹出错误的信息
                        }
                    })
                }
            })
        }

    }
}

</script>
<style scoped></style>