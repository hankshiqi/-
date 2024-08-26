<!--  -->
<template>
  <div class="main-content">
    <div class="card" style="display: flex;align-items: center;margin-bottom:10px ;" >
        <img :src="detailData.cover" alt="" style="border-radius: 5px; margin-right: 20px;">
        <div style="flex: 1;">
            <h2 style="font-size: 30px;margin-bottom: 15px;" >{{detailData.name}}</h2>
            <p style="font-size: 16px; color: #666;margin-bottom: 10px;">{{detailData.descr}}</p>
            <p style="font-size: 16px; color: #666;margin-bottom: 10px;"><i class="el-icon-date"></i> 时间:{{detailData.start}} 至 {{detailData.end}}</p>
            <p style="font-size: 16px; color: #666;margin-bottom: 10px;" v-if="detailData.form==='线上活动'"><i class="el-icon-position"></i>
                 地址:<a :href="detailData.address" target="_blank">{{detailData.address}}</a>
                 <el-tag style="margin-left: 10px;">{{detailData.form}}</el-tag>
            </p>
            <p style="font-size: 16px; color: #666;margin-bottom: 10px;" v-if="detailData.form==='线下活动'">
                <i class="el-icon-position"></i> 地址:{{detailData.address}}
                <el-tag style="margin-left: 10px;">{{detailData.form}}</el-tag>
            </p>
            <el-button type="primary" disabled v-if="detailData.due" key="已结束">已结束</el-button>
            <el-button type="primary" disabled v-else-if="detailData.sign" key="已报名">已报名</el-button>
            <el-button type="primary" v-else @click="activitySignUp">报名</el-button>
            <el-button type="primary" v-if="detailData.sign" @click="cancel">取消报名</el-button>
            <div display="flex" style="margin-top:10px ;">
                <el-button type="success" style="margin-right: 10px;" @click="changeLikes" v-if="!detailData.isLiked">点赞 {{likeCount}}</el-button>
                <el-button type="success" style="margin-right: 10px;" @click="changeLikes" v-else>已点赞 {{likeCount}}</el-button>
                <el-button type="warning" style="margin-right: 10px;" @click="changeCollects" v-if="!detailData.isCollected">收藏 {{collectCount}}</el-button>
                <el-button type="warning" style="margin-right: 10px;" @click="changeCollects" v-else>已收藏 {{collectCount}}</el-button>
                <el-button type="info" style="margin-right: 10px;">阅读量 {{detailData.readCount}}</el-button>
            </div>
        </div>
    </div>

    <div class="card" style="margin-bottom: 20px;">
    <div class="w-e-text" >
        <div style="padding-left: 10px; border-left: 5px solid #0066bc;font-size: 24px; font-weight: bold;margin-bottom: 10px;">活动详情</div>
        <div v-html="detailData.content"></div>
      </div>
    </div>

    <Comment :id="id" module="活动"/>
  </div>
</template>

<script>
import Comment from '@/components/Comment.vue';
export default {
  name: 'ActivityDetail',
  data () {
    return {
        id:this.$route.params.id,
        detailData: {},
        user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
        likeCount: 0,
        collectCount: 0,
    };
  },

  components: {Comment},

  computed: {},
  created(){
    
  },
  mounted(){
    this.$request.put('/activity/updateReadCount/'+this.id).then(res => {
        this.selectById(this.id);
    })
   
  },

  methods: {
    selectById(id){
        this.$request.get('/activity/selectById/'+id)
       .then(res => {
            this.detailData = res.data;
            this.likeCount = res.data.likeCount;
            this.collectCount = res.data.collectCount;
        })
    },
    activitySignUp(){
        this.$request.post('/activitySign/add',{userId:this.user.id,actId:this.id}).then(res => {
            if(res.code === '200'){
                this.$message.success('报名成功');
                this.selectById(this.id);
            }else{
                this.$message.error('报名失败');
            }
        })
    },
    changeLikes() {
        this.$request.post('/likes/changeLikes', {
            fid: this.id,
            module: '活动'
        }).then(res => {
            this.selectById(this.id);
        })
    },
    //点击收藏按钮
    changeCollects() {
        this.$request.post('/collects/changeCollects', {
            fid: this.id,
            module: '活动'
        }).then(res => {
            this.selectById(this.id);
        })
    },
    cancel(){
        this.$confirm('确定取消报名吗？','取消报名',{type:'warning'}).then(() => {  
            this.$request.post('/activitySign/cancel',{userId:this.user.id,actId:this.id}).then(res => {
                if(res.code === '200'){
                    this.$message.success('取消报名成功');
                    this.selectById(this.id);
                }else{
                    this.$message.error('取消报名失败');
                }
            })
        }).catch(() => {
        })
    }


  }
}

</script>
<style scoped>
</style>