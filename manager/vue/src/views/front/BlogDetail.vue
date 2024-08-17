<!--  -->
<template>
    <div class="main-content">
        <div style="display: flex;grid-gap:10px">
            <div display="flex:1" style="min-width: 80%;">
                <div class="card" style="margin-bottom: 10px;">
                    <div style="margin: 10px 0; font-size: 30px; font-weight: bold;">
                        {{ blogInfo.title }}
                    </div>
                    <div style="color: #666; margin-bottom: 20px;">
                        <span style="margin-right: 20px;"><i class="el-icon-user"></i> {{ blogInfo.userName }}</span>
                        <span style="margin-right: 20px"><i class="el-icon-date"></i> {{ blogInfo.date }}</span>
                        <span><i class="iconfont icon-dianzan"></i> 100</span>
                    </div>
                    <div class="w-e-text">
                        <div v-html="content"></div>
                    </div>
                </div>
                <!--点赞、收藏按钮-->
                <div class="card" style="text-align: center; font-size: 30px; color: #888; margin-bottom: 10px;">
                    <span :class="{ activeCheck: blogInfo.isCollected }" style="cursor: pointer;"
                        @click="changeCollects">
                        <i class="el-icon-star-off"></i>
                        {{ blogInfo.collectsNum }}
                    </span>
                    <span :class="{ activeCheck: blogInfo.isLiked }" style="margin-right: 20px; cursor: pointer;"
                        @click="changeLikes">
                        <i class="iconfont icon-dianzan" style="font-size: 34px;"></i>
                        {{ blogInfo.likesNum }}
                    </span>
                </div>

                <!--评论开始-->
                <div class="card">
                    <h2>评论 {{totalNum}}</h2>
                    <div style="margin: 20px 0;">
                        <el-input type="textarea" placeholder="请输入评论内容" v-model="commentContent"
                            style="margin: 10px 0;">
                        </el-input>
                        <el-button type="primary" @click="addComment" style="text-align: right;">发表评论</el-button>
                    </div>
                    <div style="margin: 20px 0; display: flex;" v-for="comment in commentList" :key="comment.id">
                        <img :src="comment.userAvatar" alt=""
                            style="width: 50px; height: 50px; border-radius: 50%; margin-right: 20px;">
                        <div style="flex: 1;align-items: center;">
                            <div style="margin-bottom: 10px;">
                                <div style="font-size: 16px; color: #888;">{{ comment.userName }}</div>
                                <div style="margin: 10px 0;">{{ comment.content }}</div>
                                <div style="font-size: 14px; color: #888;">{{ comment.time }}
                                    <span style="cursor: pointer;" @click="changeReply(comment)" v-if="userLocal.id !== comment.userId"><i
                                            :style="{ color: comment.atReply ? '#59aeff' : '#888' }"
                                            class="el-icon-s-comment"></i> 评论</span>
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
                                            <span style="cursor: pointer;" @click="changeReply(child)" v-if="userLocal.id !== child.userId">
                                                <i :style="{ color: child.atReply ? '#59aeff' : '#888' }"
                                                    class="el-icon-s-comment"></i> 评论
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

            <div style="min-width: 260px;">
                <div class="card" style="margin-bottom: 10px;">
                    <div style="display: flex;align-items: center;margin: 10px 0;">
                        <img v-if="blogInfo.user" :src="blogInfo.user.avatar" alt="" style="width: 50px;
                            height: 50px; border-radius: 50%;">
                        <div v-if="blogInfo.user" style=" font-size: 20px; font-weight: bold;margin-left: 10px;">
                            <div style="font-size: 20px;">{{ blogInfo.user.name }}</div>
                            <div style="color: #666;font-size: 12px;" class="line2">{{ blogInfo.user.info }}</div>
                        </div>
                    </div>

                    <div style="display: flex;text-align: center;">
                        <div style="flex: 1;">
                            <div>文章</div>
                            <div>{{ totalBlogsNum }}</div>
                        </div>
                        <div style="flex: 1;">
                            <div>点赞</div>
                            <div>{{ totalLikesNum }}</div>
                        </div>
                        <div style="flex: 1;">
                            <div>收藏</div>
                            <div>{{ totalCollectsNum }}</div>
                        </div>
                    </div>
                </div>

                <div class="card" style="margin-bottom: 10px;">

                    <div
                        style="padding-bottom: 10px;font-size: 20px; font-weight: bold; border-bottom: 1px solid #aaa;">
                        相关推荐</div>

                    <div style="margin: 10px 0;line-height: 20px;" v-for="blog in recommentBlogs" :key="blog.id">
                        <a :href="'/front/blogDetail?blogId=' + blog.id" target="_blank">
                            <div style="font-size: 15px;" class="line2">{{ blog.title }}</div>
                            <div style="font-size: 12px; color: #888;">
                                <span style="margin-right:10px;">阅读 {{ blog.readCount }}</span>
                                <span>点赞 10</span>
                            </div>
                        </a>
                    </div>

                </div>

                <div class="card" style="display: flex;">
                    <div display="flex:1"
                        style="margin-right: 10px; display: flex;flex-direction: column;justify-content: center;">
                        <div style="font-size: 20px; margin-bottom: 10px;">找对属于你的学习圈子</div>
                        <div style="font-size: 14px; color: #888;">学习精品项目</div>
                    </div>
                    <img src="@/assets/imgs/image1.png" alt="" width="70px" height="70px">
                </div>
            </div>

        </div>
    </div>
</template>

<script>
import E from "wangeditor"
import hljs from 'highlight.js'
export default {
    name: 'HelloWorld',
    data() {
        return {
            blogId: this.$route.query.blogId,
            content: '',
            blogInfo: {},
            recommentBlogs: [],
            totalBlogsNum: 0,
            totalLikesNum: 0,
            totalCollectsNum: 0,
            commentContent: '',
            commentList: [],
            totalNum: 0,
            userLocal: JSON.parse(localStorage.getItem('xm-user') || '{}'),
        };
    },

    components: {},

    computed: {},

    mounted() {
        this.getBlogInfo();
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
        getBlogInfo() {
            this.$request.get('/blog/selectById/' + this.blogId).then(res => {
                this.content = res.data.content;
                this.blogInfo = res.data;
                this.getRecommendBlogs();
                this.getTotalBlogsNum();
                this.getTotalLikes();
                this.getTotalCollects();
            })
        },
        getRecommendBlogs() {
            this.$request.post('/blog/selectRecommendBlog', this.blogInfo).then(res => {
                this.recommentBlogs = res.data;
            })
        },
        getTotalBlogsNum() {
            this.$request.get('/blog/countOfOneAuthorBlogs/' + this.blogInfo.userId).then(res => {
                this.totalBlogsNum = res.data;
            })
        },
        getTotalLikes() {
            this.$request.get('/blog/countOfOneAuthorLikes/' + this.blogInfo.userId).then(res => {
                this.totalLikesNum = res.data;
            })
        },
        getTotalCollects() {
            this.$request.get('/blog/countOfOneAuthorCollects/' + this.blogInfo.userId).then(res => {
                this.totalCollectsNum = res.data;
            })
        },
        //点击点赞按钮
        changeLikes() {
            this.$request.post('/likes/changeLikes', {
                fid: this.blogId,
                module: '文章'
            }).then(res => {
                this.getBlogInfo();
            })
        },
        //点击收藏按钮
        changeCollects() {
            this.$request.post('/collects/changeCollects', {
                fid: this.blogId,
                module: '文章'
            }).then(res => {
                this.getBlogInfo();
            })
        },
        addComment() {
            this.$request.post('/comment/add', {
                fid: this.blogId,
                content: this.commentContent,
                module: '文章'
            }).then(res => {
                //console.log(res.data)
                this.commentContent = '';
                this.loadComment();
            })
        },
        loadComment() {
            this.$request.get('/comment/selectForUser', {
                params: {
                    fid: this.blogId,
                    module: '文章'
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
                    fid: this.blogId,
                    module: '文章'
                }}).then(res => {
                this.totalNum = res.data;
            })
        }
    }
}

</script>
<style>
/* blockquote 样式 */
blockquote {
    display: block;
    border-left: 8px solid #d0e5f2;
    padding: 20px 10px;
    margin: 10px 0;
    line-height: 1.4;
    font-size: 100%;
    background-color: #f1f1f1;
}

/* code 样式 */
code {
    display: inline-block;
    display: inline;
    zoom: 1;
    background-color: #f1f1f1;
    border-radius: 3px;
    padding: 3px 5px;
    margin: 0 3px;
}

pre code {
    display: block;
}

p {
    line-height: 30px
}

a {
    color: #333;
}

.activeCheck {
    color: orange;
}
</style>