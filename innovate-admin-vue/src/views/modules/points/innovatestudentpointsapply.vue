<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.stuNum" placeholder="学号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('points:innovatestudentpointsapply:save')" type="primary" @click="addOrUpdateHandle()">
          我要申请
        </el-button>
        <el-button v-if="isAuth('points:innovatestudentpointsapply:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
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
        sortable
        hidden
        type="expand"
        prop="applyStatus"
        header-align="center"
        align="center"
        width="120"
        label="展开流程进度">
        <template slot-scope="props">
          <el-row>
            <el-card style=": 0.1rem">
              <el-col :span="3">
                <el-tag>积分申请进度</el-tag>
              </el-col>
              <el-col :span="21">
                <el-steps
                  :active="props.row.applyStatus"
                  finish-status="success">
                  <el-step title="待提交"></el-step>
                  <el-step title="二级学院审批"></el-step>
                  <el-step title="审批完成"></el-step>
                </el-steps>
              </el-col>
            </el-card>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column
        prop="stuNum"
        header-align="center"
        align="center"
        label="学号">
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
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small"
                     v-if="$store.state.user.id === scope.row.applyUserId && (scope.row.applyStatus === 0 || scope.row.applyStatus === -1)"
                     @click="editApplyStatus(scope.row, 1)">提交申请
          </el-button>
          <el-button type="text" size="small" @click="detailInfo(scope.row.integralApplyId)">查看</el-button>
          <el-button type="text" size="small"
                     v-if="(scope.row.applyStatus === 0 || scope.row.applyStatus === -1) && $store.state.user.id === scope.row.applyUserId"
                     @click="addOrUpdateHandle(scope.row.integralApplyId)">修改
          </el-button>
          <el-button type="text" size="small"
                     v-if="$store.state.user.id === scope.row.applyUserId && scope.row.applyStatus === 0"
                     @click="deleteHandle(scope.row.integralApplyId)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <detail-info v-if="detailInfoVisible" ref="detailInfo"></detail-info>
  </div>
</template>

<script>
  import AddOrUpdate from './innovatestudentpointsapply-add-or-update'
  import DetailInfo from './apply/detail-info'

  export default {
    data() {
      return {
        dataForm: {
          stuNum: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        detailInfoVisible: false
      }
    },
    components: {
      AddOrUpdate,
      DetailInfo
    },
    activated() {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList() {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/points/innovatestudentpointsapply/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'stuNum': this.dataForm.stuNum,
            'isDel': 0,
            'noPass': 'noPass',
            'noApply': this.isAuth('points:innovatestudentpointsapply:adminApply') === true ? "noApply" : null,
            'apply_user_id': this.isAuth('points:innovatestudentpointsapply:adminApply') === true ? null : this.$store.state.user.id,
            'instituteId': this.isAuth('points:pointsApply:adminApply') === true ? null : this.$store.state.user.instituteId
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      // 每页数
      sizeChangeHandle(val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle(val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle(val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle(id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
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
        this.$confirm(`你确定提交申请类型为["${row.participateType}"]的记录吗？`, '提示', {
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
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },
      // 删除
      deleteHandle(id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.integralApplyId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/points/innovatestudentpointsapply/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              if (status == 3) {
                let pointsReason = this.dataForm.participateType + '-' + this.dataForm.raceGrade
                if (this.dataForm.prizeGrade != '' && this.dataForm.prizeGrade != null)
                  pointsReason = pointsReason + '-' + this.dataForm.prizeGrade
                if (this.dataForm.persionType != '' && this.dataForm.persionType != null) {
                  pointsReason = pointsReason + '-' + this.dataForm.persionType
                  this.$http({
                    url: this.$http.adornUrl('/points/innovatestudentpoints/saveApplyPoints'),
                    method: 'post',
                    data: this.$http.adornData({
                      stuNum: this.dataForm.userEntity.username, // 学号
                      points: this.dataForm.applyIntegral, // 积分
                      pointsReason: pointsReason
                    })
                  })
                }
              }
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
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
<style>
  .el-step__title {
    font-size: 12px;
    line-height: 28px;
  }

  .el-table__expanded-cell[class*=cell] {
    padding: 5px 5px;
  }
</style>
