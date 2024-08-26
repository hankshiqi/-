<!--  -->
<template>
    <div>
      <el-row :gutter="10">
          <el-col :span="span" v-for="item in tableData" :key="item.id">
              <div class="card" style="margin-bottom:10px ;">
                <img :src="item.cover" alt="" width="100%" @click="viewActivityDetail(item.id)" style="cursor: pointer;">
                <div style="font-weight: bold;">{{item.name}}</div>
                <div style="display: flex; align-items: center; justify-content: space-between; margin-top: 10px;color: #888;">
                  <div><i class="el-icon-date"></i> {{item.start}}</div>
                  <el-button type="primary" v-if="item.due==true" disabled key="已截止">已截止</el-button>
                  <el-button type="primary" v-else @click="viewActivityDetail(item.id)">报名</el-button>
                </div>
              </div>
          </el-col>
          
      </el-row>
      <div class="pagination" v-if="total > 0">
          <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum"
            :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
          </el-pagination>
    </div>
    <div style="width: 100%;display: flex;flex: 1;text-align: center;font-size: 30px;justify-content: center;align-items: center;"
      class="card" v-if="total === 0">
      暂无数据
    </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ActivityList',
    data () {
      return {
        tableData: [],  // 所有的数据
        pageNum: 1,   // 当前的页码
        pageSize: 8,  // 每页显示的个数
        total: 0,
      };
    },
    props: {
        type:null, // 类型
        span:null,
    },
  
    components: {},
  
    computed: {},
  
    mounted(){
      this.load(1)
  
    },
  
    methods: {
      load(pageNum) {  // 分页查询
        if (pageNum) this.pageNum = pageNum
        let url;
        switch (this.type) {
            case 'user':
                url = '/activity/selectPageByUser'
                break;
            case 'collect':
                url = '/activity/selectPageCollect'
                break;
            default:
                url = '/activity/selectPage'
                break;
        }
        this.$request.get(url, {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          }
        }).then(res => {
          this.tableData = res.data?.list
          this.total = res.data?.total
        })
      },
      handleCurrentChange(pageNum) {
        this.load(pageNum)
      },
      viewActivityDetail(id) {
        this.$router.push(`/front/activityDetail/${id}`)
      }
    }
  }
  
  </script>
  <style scoped>
  </style>