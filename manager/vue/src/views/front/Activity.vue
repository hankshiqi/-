<!--  -->
<template>
  <div class="main-content">
    <activity-list :span="6"/>
  </div>
</template>

<script>
import ActivityList from '@/components/ActivityList.vue';
export default {
  name: 'Activity',
  data () {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 8,  // 每页显示的个数
      total: 0,
    };
  },

  components: {ActivityList},

  computed: {},

  mounted(){
    this.load(1)

  },

  methods: {
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/activity/selectPage', {
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