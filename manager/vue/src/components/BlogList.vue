<!--  -->
<template>
  <div>
    <div style="flex: 1;justify-content: center;" class="card" v-if="total > 0">
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
              <span style="margin-right: 20px"><i class="iconfont icon-dianzan"></i> {{item.likesNum}}</span>
              <span style="color: royalblue; cursor: pointer;margin-right: 20px;" @click="editBlog(item.id)" v-if="showEdit"><i class="el-icon-edit"></i></span>
              <span style="color: red; cursor: pointer;" @click="deleteBlog(item.id)"><i class="el-icon-delete" v-if="showEdit"></i></span>
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
    <div style="width: 100%;display: flex;flex: 1;text-align: center;font-size: 30px;justify-content: center;align-items: center;"
      class="card" v-if="total === 0">
      暂无数据
    </div>
  </div>
</template>

<script>
export default {
  name: 'BlogList',
  props:{
    currentCategory:null,
    title:null,
    type:null,
    showEdit:false,
  },
  data () {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 5,  // 每页显示的个数
      total: 0,
    };
  },

  components: {},

  computed: {},

  mounted(){
    this.loadBlogs(1)
  },
  watch:{
    currentCategory: function(val) {
      this.loadBlogs(1)
    }
  },

  methods: {
    loadBlogs(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      let url;
      switch (this.type) {
        case 'user':
          url = '/blog/selectPageByUser' //查询某个用户的博客
          break
        case 'collect':
          url = '/blog/selectPageCollect' //查询某个用户收藏的博客
          break
        default:
          url = '/blog/selectPage'
          break
      }
      this.$request.get(url, {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          categoryName: this.currentCategory === '全部博客' ? null : this.currentCategory,
          title: this.title
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
    deleteBlog(blogId) {
      this.$confirm('确认删除该博客吗？')
       .then(() => {
          this.$request.delete('/blog/delete/'+blogId).then(res => {
            this.$message.success('删除成功')
            this.loadBlogs(this.pageNum)
          })
        })
       .catch(() => {
       })
      },
      editBlog(blogId) {
        window.open('/front/newBlog?blogId='+blogId)
      }

  }
}

</script>
<style scoped>
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