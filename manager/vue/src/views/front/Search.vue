<!--  -->
<template>
  <div style="width: 55%; margin: 0 auto; padding-top: 10px;">
    <blog-list :title="title"/>
  </div>
</template>

<script>
import BlogList from '@/components/BlogList.vue';
export default {
  name: 'Search',
  data () {
    return {
        title:this.$route.query.title
    };
  },

  components: {BlogList},

  computed: {},

  mounted(){},

  methods: {}
}

</script>
<style scoped>
</style>