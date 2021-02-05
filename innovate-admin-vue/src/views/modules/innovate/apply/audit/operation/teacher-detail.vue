<template>
  <el-dialog
    :title="'查看未评分评委'"
    width="80%"
    v-loading="dataListLoading"
    @close="closeDialog"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-row>
      <table border="1" cellspacing="0" width="100%" class="table">
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr class="contents" align="center">
          <th colspan="10">未评分评委老师信息</th>
        </tr>
        <tr align='center'>
          <td colspan="10" style="height: 1.2rem"></td>
        </tr>
        <tr align='center'>
          <th>姓名</th>
          <th>性别</th>
          <th colspan="2">所在单位/二级学院</th>
          <th>职务</th>
          <th>职称</th>
          <th colspan="2">邮箱</th>
          <th colspan="2">联系电话</th>
        </tr>
        <template v-for="item in noScoreTeacher">
          <tr align="center">
            <td v-text="item.name"></td>
            <td v-for="sex in sexList"
                :key="sex.value"
                v-if="item.teacherSex === sex.value"
                v-text="sex.label">
            </td>
            <td colspan="2">
              <span v-for="institute in instituteList" v-if="item.instituteId === institute.instituteId" v-text="institute.instituteName"></span>
            </td>
            <td v-text="item.teacherPost"></td>
            <td colspan="1">
              <span v-for="teacherTitle in teacherTitleList" v-if="item.teacherTitle === teacherTitle.titleId" v-text="teacherTitle.titleName"></span>
            </td>
            <td colspan="2">{{item.email}}</td>
            <td colspan="2">{{item.mobile}}</td>
          </tr>
        </template>
      </table>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "TeacherDetail",
  data() {
    return{
      visible: false,
      noScoreTeacher: [], // 评委老师信息
      dataListLoading: false,
      instituteList: this.$store.state.user.institute,
      sexList: [
        {value: 1, label: '男'}, {value: 2, label: '女'}
      ],
      teacherTitleList: this.$store.state.user.title,
      dataForm: {
        id: 0,
      },
    }
  },
  methods:{
    init(id){
      this.visible = true
      this.dataListLoading = true
      this.dataForm.id = id || 0
      if (this.dataForm.id) {
        this.$http({
          url: this.$http.adornUrl(`/innovate/declare/review/noScoreTeacher`),
          method: 'get',
          params: this.$http.adornParams({
            'declareId': this.dataForm.id
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
           this.noScoreTeacher = data.noScoreTeacher
            this.dataListLoading = false
          }
        })
      } else {
        this.dataListLoading = false
      }
    },
    closeDialog () {
      this.visible = false
      this.$emit('refreshDataList')
    }
  }
}
</script>

<style>
.contents{
  height: 60px;
  font-size: 18px;
}
.el-card__body {
  padding: 10px;
}
.el-step__title {
  font-size: 12px;
  line-height: 28px;
}
.table {
  border: 1px solid #ddd;
  /*为表格设置合并边框模型*/
  border-collapse: collapse;
  /*列宽由表格宽度和列宽度设定*/
  table-layout: fixed;
}
.table td {
  /*允许在单词内换行。*/
  word-break: break-word;
  /*设置宽度*/
  width: 80%;
}
.table th {
  /*允许在单词内换行。*/
  word-break: break-word;
  /*设置宽度*/
  width: 80%;
}
</style>
