<template>
  <div class="main-content" style="display: flex;grid-column-gap: 10px;align-items: flex-start; justify-content: space-between;">
    <div style="width: 150px;" class="card">
      <div class="category-item" :class="{ 'category-item-click': item.name === currentCategory }"
        v-for="item in categoryList" :key="item.id" @click="changeCurrentCategory(item.name)">
        {{ item.name }}
      </div>
    </div>
    <!--博客列表-->
    <BlogList :currentCategory="currentCategory"/>
    <!--右侧栏-->
    <div>
      <div style="width: 260px;">
        <!--欢迎语-->
        <div class="card" style="margin-bottom: 10px">
          <div style="font-size: 20px; font-weight: bold; margin-bottom: 10px">欢迎您！😊</div>
          <a href="/front/person">
            <div style="color: #666">写下博客记录美好的一天</div>
          </a>
        </div>
        <!--榜单-->
        <div style="margin: 10px 0;" class="card">
          <div style="display: flex; align-items: center; padding-bottom: 10px; border-bottom: 1px solid #666;">
            <div style="flex: 1;font-size: 20px;font-weight: bold;">文章榜单</div>
            <div @click="getTopBlogs" style="cursor: pointer;color: #666;"><i class="el-icon-refresh"></i>换一换</div>
          </div>
          <div v-for="blog in topBlogsList" :key="blog.id" style="margin: 20px 0;">
            <a :href="'/front/blogDetail?blogId='+blog.id" target="_blank">
              <div class="showdescr" style="margin: 10px 0;">
              <span style="color: orangered;" v-if="blog.index === 1">{{ blog.index }}</span>
              <span style="color: goldenrod;" v-else-if="blog.index === 2">{{ blog.index }}</span>
              <span style="color: dodgerblue;" v-else-if="blog.index === 3">{{ blog.index }}</span>
              <span style="color: #666;" v-else>{{ blog.index }}</span>
              <span style="margin-left:5px;">{{ blog.title }}</span>
            </div>
          </a>  
          </div>
        </div>
        <!--最热活动-->
        <div v-for="act in topActivityList" :key="act.id">
          <img :src="act.cover" alt="" width="260px" style="border-radius: 5px; cursor: pointer;" @click="goActivityDetail(act.id)">
        </div>
        <!--底部信息-->
        <div style="margin: 10px 0;color: #666;font-size: 14px; 
        max-width: 260px;">
          <div style="margin: 10px 0;">举报邮箱:1951394181@qq.com</div>
          <div style="margin: 10px 0;">举报电话:13518246012</div>
          <div style="margin: 10px 0;">川ICP备888888©2024 hankyang</div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import BlogList from '../../components/BlogList'

export default {
  components:{
    BlogList
  },
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 5,  // 每页显示的个数
      total: 0,
      categoryList: [],
      currentCategory: '全部博客',
      tagsArr: [],
      lastIndex: 0,
      topBlogsList: [],
      maxIndex: 0,
      topActivityList: []
    }
  },
  mounted() {
    this.load(),
    this.getTopBlogs(),
    this.getTopActivity()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    load() {
      this.$request.get('/category/selectAll').then(res => {
        this.categoryList = res.data || []
        this.categoryList.unshift({ id: 0, name: '全部博客' })
      })
    },
    changeCurrentCategory(name) {
      this.currentCategory = name
    },
    getTopBlogs() {
      this.$request.get('/blog/selectTop').then(res => {
        this.topBlogsList = res.data || []
        this.maxIndex = res.data.length
        let i = 1
        this.topBlogsList.forEach(blog => {
          blog.index = i++
        })
        if (this.lastIndex >= this.maxIndex) {
          this.lastIndex = 0
        }
        this.topBlogsList = this.topBlogsList.slice(this.lastIndex, this.lastIndex + 5)
        this.lastIndex += 5
      })
    },
    getTopActivity() {
      this.$request.get('/activity/selectTopActivity').then(res => {
        this.topActivityList = res.data || []
      })
    },
    
    goActivityDetail(id){
      window.open('/front/activityDetail/'+id)
    }

  }
}
</script>
<style scoped>
.category-item {
  text-align: center;
  margin: 0 auto;
  cursor: pointer;
  height: 30px;
  line-height: 30px;
}

.category-item-click {
  background-color: #59aeff !important;
  color: #fff !important;
  border-radius: 6px;
}

.showdescr {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
a{
  color: #333;
}
</style>
