<template>
  <div class="main-content" style="display: flex;grid-column-gap: 10px;align-items: flex-start;">
    <div style="width: 150px;" class="card">
      <div class="category-item" :class="{ 'category-item-click': item.name === currentCategory }"
        v-for="item in categoryList" :key="item.id" @click="changeCurrentCategory(item.name)">
        {{ item.name }}
      </div>
    </div>
    <div style="flex: 1;" class="card" v-if="total > 0">
      <div style="padding: 10px 0; 
      border-bottom: 1px solid #666;
      margin: 10px 0;
      display: flex;
      max-width: 850px;" v-for="item in tableData" :key="item.id">
        <div style="flex: 1;max-width: 700px;">
          <a :href="'/front/blogDetail?blogId='+item.id" target="_blank"><div class="blog-title">{{ item.title }}</div></a>
          <div class="showdescr" style="font-size: 14px; color:#666;margin-bottom: 10px;">
            {{ item.descr }}</div>
          <div style="margin-bottom: 10px ;display: flex;">
            <div style="flex:1;color: #666;">
              <span style="margin-right: 20px;"><i class="el-icon-user"></i> {{ item.userName }}</span>
              <span style="margin-right: 20px"><i class="iconfont icon-yanjing"></i> {{ item.readCount }}</span>
              <span><i class="iconfont icon-dianzan"></i> {{item.likesNum}}</span>
            </div>
            <div style="display: flex;grid-column-gap:5px;">
              <el-tag type="primary" v-for="tag in item.tags" :key="tag">{{ tag }}</el-tag>
            </div>
          </div>
        </div>
        <div style="width: 100px;margin-left: 40px;">
          <img :src="item.cover" alt="" style="width: 100%;">
        </div>
      </div>
      <div v-if="total > 0">
        <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
        </el-pagination>
      </div>
    </div>
    <div style="display: flex;flex: 1;text-align: center;font-size: 30px;justify-content: center;align-items: center;"
      class="card" v-if="total === 0">
      暂无数据
    </div>
    <div>
      <div style="width: 260px;">
        <div class="card" style="margin-bottom: 10px">
          <div style="font-size: 20px; font-weight: bold; margin-bottom: 10px">欢迎您！😊</div>
          <a href="/front/person">
            <div style="color: #666">写下博客记录美好的一天</div>
          </a>
        </div>
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
        <div v-for="act in topActivityList" :key="act.id">
          <img :src="act.cover" alt="" width="260px" style="border-radius: 5px;">
        </div>
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

export default {

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
      this.loadBlogs(1),
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
      this.loadBlogs(1)
    },
    loadBlogs(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/blog/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          categoryName: this.currentCategory === '全部博客' ? null : this.currentCategory
        }
      }).then(res => {
        const blogsWithTags = res.data.list.map(blog => ({
          ...blog,
          tags: JSON.parse(blog.tags || '[]')
        }));
        this.tableData = blogsWithTags;
        this.total = res.data.total;
      })
    },
    handleCurrentChange(pageNum) {
      this.loadBlogs(pageNum)
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
.blog-title {
  font-weight: bold;
  font-size: 20px;
  margin-bottom: 10px;
  cursor: pointer;
}
.blog-title:hover {
  color: #59aeff;
}
a{
  color: #333;
}
</style>
