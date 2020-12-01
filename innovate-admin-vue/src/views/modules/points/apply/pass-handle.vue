<template>
  <el-dialog
    title="详情"
    :close-on-click-modal="true"
    :visible.sync="visible"
    width="80%"
  >
    <h2 style="text-align: center">该类型该学生积分申请记录</h2>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        type="index"
        header-align="center"
        align="center"
        label="ID">
      </el-table-column>
      <el-table-column
        prop="participateType"
        header-align="center"
        align="center"
        label="申请类型">
      </el-table-column>
      <el-table-column
        prop="raceGrade"
        header-align="center"
        align="center"
        label="项目名称">
      </el-table-column>
      <el-table-column
        prop="persionType"
        header-align="center"
        align="center"
        label="参与人类别"
      >
        <template slot-scope="scope">
          {{scope.row.persionType? (scope.row.persionType == 1 ?'负责人':'参与成员'): '-'}}
        </template>
      </el-table-column>
      <el-table-column
        prop="prizeGrade"
        header-align="center"
        align="center"
        label="奖项等级">
      </el-table-column>
      <el-table-column
        prop="applyIntegral"
        header-align="center"
        align="center"
        label="申请积分">
      </el-table-column>
      <el-table-column
        prop="applyTime"
        header-align="center"
        align="center"
        label="申请时间">
      </el-table-column>
      <el-table-column
        prop="applyStatus"
        header-align="center"
        align="center"
        label="状态">
        <template slot-scope="scope">
          <el-tag>{{scope.row.applyStatus == 3? '通过' : '未通过'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="detailInfo(scope.row.integralApplyId)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: center;margin-top: 100px">
      <el-button type="success" @click="editApplyStatus(dataForm, 3)">通过</el-button>
      <el-button type="danger" @click="editApplyStatus(dataForm, -1)">不通过</el-button>
    </div>
    <!-- 弹窗, 新增 / 修改 -->
    <detail-info v-if="detailInfoVisible" ref="detailInfo"></detail-info>
  </el-dialog>
</template>

<script>
  import DetailInfo from './detail-info'

  export default {
    data() {
      return {
        dataForm: {},
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        visible: false,
        dataListLoading: false,
        detailInfoVisible: false
      }
    },
    components: {
      DetailInfo
    },
    methods: {
      // 获取数据列表
      getDataList(row) {
        this.dataForm = row
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/points/innovatestudentpointsapply/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'applyStatus': 3,
            'apply_user_id': row.applyUserId,
            'sysPointsId': row.sysPointsId
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
            this.visible = true
            if (data.page.totalCount == 0) {
              this.visible = false
              this.editApplyStatus(row, 3)
            }
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 查看
      detailInfo(id) {
        this.detailInfoVisible = true
        this.$nextTick(() => {
          this.$refs.detailInfo.init(id)
        })
      },
      // 提交申请
      editApplyStatus(row, status) {
        var title = '通过'
        if (status == -1) {
          title = '不通过'
        }
        this.$confirm(`你确定给予申请类型为["${row.participateType}"]的申请${title}吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/points/innovatestudentpointsapply/update'),
            method: 'post',
            data: this.$http.adornData({
              pointsApplyEntity: {
                'integralApplyId': row.integralApplyId || undefined,
                'applyStatus': status
              }
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              if (status == 3) {
                let pointsReason = this.dataForm.participateType + '-' + this.dataForm.raceGrade
                if (this.dataForm.prizeGrade != '' && this.dataForm.prizeGrade != null)
                  pointsReason = pointsReason + '-' + this.dataForm.prizeGrade
                if (this.dataForm.persionType != '' && this.dataForm.persionType != null) {
                  let name = '负责人'
                  if (this.dataForm.persionType == 2) {
                    name = '参与人员'
                  }
                  pointsReason = pointsReason + '-' + name
                }
                this.$http({
                  url: this.$http.adornUrl('/points/innovatestudentpoints/saveApplyPoints'),
                  method: 'post',
                  data: this.$http.adornData({
                    stuNum: this.dataForm.stuNum, // 学号
                    points: this.dataForm.applyIntegral, // 积分
                    pointsReason: pointsReason
                  })
                })
              }
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      }
    }
  }
</script>
