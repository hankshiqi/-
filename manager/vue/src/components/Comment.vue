<!--  -->
<template>
  <div>
    <div class="card">
        <h2>评论 {{totalNum}}</h2>
        <div style="margin: 20px 0;">
            <el-input type="textarea" placeholder="请输入评论内容"
                v-model="commentContent"
                style="margin: 10px 0;">
            </el-input>
            <el-button type="primary" @click="validateComment" style="text-align: right;">发表评论</el-button>
        </div>
        <div style="margin: 20px 0; display: flex;" v-for="comment in commentList" :key="comment.id">
            <img :src="comment.userAvatar" alt=""
                style="width: 50px; height: 50px; border-radius: 50%; margin-right: 20px;">
            <div style="flex: 1;align-items: center;">
                <div style="margin-bottom: 10px;">
                    <div style="font-size: 16px; color: #888;">{{ comment.userName }}</div>
                    <div style="margin: 10px 0;">{{ comment.content }}</div>
                    <div style="font-size: 14px; color: #888;">{{ comment.time }}
                        <span style="cursor: pointer;" @click="changeReply(comment)" v-if="user.id !== comment.userId"><i
                                :style="{ color: comment.atReply ? '#59aeff' : '#888' }"
                                class="el-icon-s-comment"></i> 评论
                        </span>
                        <span style="cursor: pointer;" @click="del(comment.id)" v-if="user.id === comment.userId"><i
                                class="el-icon-delete"></i> 删除
                        </span>
                    </div>
                    <div v-if="comment.atReply" style="margin: 10px 0;">
                        <el-input type="textarea" placeholder="请输入回复内容" v-model="comment.replyContent"
                            style="margin: 10px 0;"></el-input>
                        <el-button type="primary" @click="addReply(comment)"
                            style="text-align: right;">回复</el-button>
                    </div>
                </div>
                <!--子评论-->
                <div style="margin: 20px 0; display: flex;" v-for="child in comment.children"
                    :key="child.id">
                    <img :src="child.userAvatar" alt=""
                        style="width: 50px; height: 50px; border-radius: 50%; margin-right: 20px;">
                    <div style="flex: 1; ">
                        <div style="margin-bottom: 10px;">
                            <div style="font-size: 16px; color: #888;">{{ child.userName }} 
                                <span
                                    style="color: #333" v-if="child.replyName !== comment.userName">回复 {{
                child.replyName }}
                                </span>
                            </div>
                            <div style="margin: 10px 0;">{{ child.content }}</div>
                            <div style="font-size: 14px; color: #888;">{{ child.time }}
                                <span style="cursor: pointer;" @click="changeReply(child)" v-if="user.id !== child.userId">
                                    <i :style="{ color: child.atReply ? '#59aeff' : '#888' }"
                                        class="el-icon-s-comment"></i> 评论
                                </span>
                                <span style="cursor: pointer;" @click="del(child.id)" v-if="user.id === child.userId"><i
                                class="el-icon-delete"></i> 删除
                                </span>
                            </div>
                            <div v-if="child.atReply" style="margin: 10px 0;">
                                <el-input type="textarea" placeholder="请输入回复内容"
                                    v-model="child.replyContent" style="margin: 10px 0;"></el-input>
                                <el-button type="primary" @click="addReply(child)"
                                    style="text-align: right;">回复</el-button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--评论结束-->

  </div>
</template>

<script>
export default {
  name: 'Comment',
  props: {
    fid:null,
    module:null
  },
  data () {
    return {
        commentContent: '',
        commentList: [],
        totalNum: 0,
        user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    };
  },

  components: {},

  computed: {},

  mounted(){
    this.loadComment();
    this.getCount();
  },

  methods: {
        addReply(comment) {
            this.$request.post('/comment/add', {
                pid: comment.id,
                content: comment.replyContent,
                rootId: comment.rootId,
                module: comment.module,
                fid: comment.fid
            }).then(res => {
                comment.replyContent = '';
                this.loadComment();
            })
        },
        addComment() {
            this.$request.post('/comment/add', {
                fid: this.fid,
                content: this.commentContent,
                module: this.module
            }).then(res => {
                //console.log(res.data)
                this.commentContent = '';
                this.loadComment();
            })
        },
        loadComment() {
            this.$request.get('/comment/selectForUser', {
                params: {
                    fid: this.fid,
                    module: this.module
                }
            }).then(res => {
                this.commentList = res.data;
            })
        },
        changeReply(comment) {
            this.$set(comment, 'atReply', !comment.atReply)
        },
        getCount(){
            this.$request.get('/comment/getCount', {
                params: {
                    fid: this.fid,
                    module: this.module
                }}).then(res => {
                this.totalNum = res.data;
            })
        },
        del(id) {   // 单个删除
        this.$confirm('您确定删除吗？', '确认删除', { type: "warning" }).then(response => {
            this.$request.delete('/comment/delete/' + id).then(res => {
            if (res.code === '200') {   // 表示操作成功
                this.$message.success('操作成功')
                this.loadComment()
            } else {
                this.$message.error(res.msg)  // 弹出错误的信息
            }
            })
        }).catch(() => {
        })  
    },
    validateComment(){
        if(this.commentContent.trim() === ''){
            this.$message.error('评论内容不能为空')
            return false;
        }
        else{
            this.addComment();
        }
    }
  }
}

</script>
<style scoped>
</style>