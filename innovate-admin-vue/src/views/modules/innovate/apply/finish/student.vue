<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-date-picker
          @change="getDataList"
          :editable="false"
          v-model="dataForm.finishTime"
          align="right"
          type="year"
          placeholder="请选择年度">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.projectName" placeholder="项目名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('innovate:finish:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('innovate:finish:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      :default-sort="{prop: 'finishInfoEntity.finishId', order: 'ascending'}"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        hidden
        header-align="center"
        align="center"
        width="50"
        label="ID">
        <template slot-scope="props">
          <p v-text="props.$index+1"></p>
        </template>
      </el-table-column>
      <el-table-column
        sortable
        hidden
        type="expand"
        prop="finishInfoEntity.finishId"
        header-align="center"
        align="center"
        width="120"
        label="展开流程进度">
        <template slot-scope="props">
          <el-row>
            <el-card style=": 0.1rem">
              <el-col :span="3">
                <el-tag>结题申请审批进度</el-tag>
              </el-col>
              <el-col :span="21">
                <el-steps
                  :active="props.row.finishInfoEntity.projectFinishApplyStatus"
                  finish-status="success">
                  <el-step title="项目负责人提交"></el-step>
                  <el-step title="指导老师审批"></el-step>
                  <el-step title="二级学院审批"></el-step>
                  <el-step title="管理员审批"></el-step>
                  <el-step title="评委审批"></el-step>
                  <el-step title="管理员审批"></el-step>
                  <!--<el-step title="超级管理员审批"></el-step>-->
                </el-steps>
              </el-col>
            </el-card>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column
        sortable
        prop="finishInfoEntity.finishName"
        header-align="center"
        align="center"
        label="申报名称">
      </el-table-column>
      <el-table-column
        sortable
        prop="finishInfoEntity.finishType"
        header-align="center"
        align="center"
        label="申报类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.finishInfoEntity.finishType === 1" size="small">创新训练项目</el-tag>
          <el-tag v-if="scope.row.finishInfoEntity.finishType === 2" size="small">创业训练项目</el-tag>
          <el-tag v-if="scope.row.finishInfoEntity.finishType === 3" size="small">创业实践项目</el-tag>
          <el-tag v-if="scope.row.finishInfoEntity.finishType === null" size="small">无</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        sortable
        prop="finishInfoEntity.finishTime"
        header-align="center"
        align="center"
        label="申报时间">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.finishInfoEntity.finishTime === null" size="small">未申报</el-tag>
          <el-tag v-if="scope.row.finishInfoEntity.finishTime != null" size="small">{{scope.row.finishInfoEntity.finishTime}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="220"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('innovate:project:list')" type="text" size="small" @click="detailHandle(scope.row.finishInfoEntity.finishId)">详情</el-button>
          <el-button v-if="addOrUpadate(scope.row.finishInfoEntity)" type="text" size="small" @click="addOrUpdateHandle(scope.row.finishInfoEntity.finishId)">修改</el-button>
          <el-button v-if="isDelete(scope.row.finishInfoEntity)" type="text" size="small" @click="deleteHandle(scope.row.finishInfoEntity.finishId)">删除</el-button>
          <br v-if="isUpadate(scope.row.finishInfoEntity)"/>
          <el-button v-if="isUpadate(scope.row.finishInfoEntity)" type="text" size="small" @click="isUpdateHandle(scope.row.finishInfoEntity.finishId)">申请修改</el-button>
          <el-button v-if="isUpadate(scope.row.finishInfoEntity)" type="text" size="small" @click="updateHistoryHandle(scope.row.finishInfoEntity.finishId)">申请记录</el-button>
          <br v-if="applyFinishIsVisible(scope.row.finishInfoEntity)">
          <el-button v-if="applyFinishIsVisible(scope.row.finishInfoEntity)" type="text" size="small" @click="applyFinishHandle(scope.row.finishInfoEntity.finishId)">提交结题申请</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[5, 10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <update-history v-if="isHistoyVisible" ref="updateHistory" @refreshDataList="getDataList"></update-history>
    <update-add-or-update v-if="isUpdateVisible" ref="isUpdate" @refreshDataList="getDataList"></update-add-or-update>
    <detail v-if="detailVisible" ref="detail" @refreshDataList="getDataList"></detail>
  </div>
</template>

<script>
  import AddOrUpdate from './project/info-add-or-update'
  import Detail from './operation/info-detail'
  import UpdateAddOrUpdate from './operation/update-add-or-update'
  import UpdateHistory from './operation/update-history'

  export default {
    data () {
      return {
        projectList: [],
        // eventLists: [],
        userTeacherInfoEntities: this.$store.state.userTeacherInfoEntities,
        dataForm: {
          projectName: '',
          baseId: '',
          finishTime: new Date(),
          idDel: 0
        },
        statusList: [
          {value: 1, label: '农、林、牧、渔业'}, {value: 2, label: '采矿业'},
          {value: 3, label: '制造业'}, {value: 4, label: '电力、热力、燃气及水的生产和供应业'},
          {value: 5, label: '环境和公共设施管理业'}, {value: 6, label: '建筑业'},
          {value: 7, label: '交通运输、仓储业和邮政业'}, {value: 8, label: '信息传输、计算机服务和软件业'},
          {value: 9, label: '批发和零售业'}, {value: 10, label: '住宿、餐饮业'},
          {value: 11, label: '金融、保险业'}, {value: 12, label: '房地产业'},
          {value: 13, label: '租赁和商务服务业'}, {value: 14, label: '科学研究、技术服务和地质勘查业'},
          {value: 15, label: '水利、环境和公共设施管理业'}, {value: 16, label: '居民服务和其他服务业'},
          {value: 17, label: '教育'}, {value: 18, label: '卫生、社会保障和社会服务业'},
          {value: 19, label: '文化、体育、娱乐业'}, {value: 20, label: '综合（含投资类、主业不明显）'},
          {value: 21, label: '其它'}
        ],
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        detailVisible: false,
        applyVisible: false,
        isUpdateVisible: false,
        isHistoyVisible: false
      }
    },
    components: {
      UpdateHistory,
      UpdateAddOrUpdate,
      AddOrUpdate,
      Detail
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.addOrUpdateVisible = false
        this.detailVisible = false
        this.isUpdateVisible = false
        this.isHistoyVisible = false
        this.$http({
          url: this.$http.adornUrl(`/innovate/finish/info/list`),
          method: 'get',
          params: this.$http.adornParams({
            'projectName': this.dataForm.projectName,
            'finishTime': this.dataForm.finishTime == null ? '' : this.dataForm.finishTime.getFullYear(),
            'currPage': this.pageIndex,
            'pageSize': this.pageSize,
            'userId': this.$store.state.user.id,
            'noPass': 'finish_no_pass',
            'noPassStatus': 0,
            // 'isTeacher': true
            'isStudent': true,
            // 'apply': 'project_audit_apply_status',
            // 'applyStatus': 3,
            'isDel': 0
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
            // for (let index = 0; index < this.dataList.length; index++) {
            //   if (this.applyFinishIsVisible(this.dataList[index].finishInfoEntity) === true) {
            //     this.applyFinishHandle(this.dataList[index].finishInfoEntity.finishId)
            //   }
            // }
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
        // this.$http({
        //   url: this.$http.adornUrl('/innovate/finish/info/list'),
        //   method: 'get',
        //   params: this.$http.adornParams({
        //     'username': this.dataForm.userName,
        //     'currPage': this.pageIndex,
        //     'pageSize': this.pageSize,
        //     'userId': this.$store.state.user.id,
        //     // 'isTeacher': true
        //     'isStudent': true,
        //     // 'apply': 'project_audit_apply_status',
        //     // 'applyStatus': 3,
        //     'isDel': 0
        //   })
        // }).then(({data}) => {
        //   if (data && data.code === 0) {
        //     this.dataList = data.page.list
        //     this.totalPage = data.page.totalCount
        //   } else {
        //     this.dataList = []
        //     this.totalPage = 0
        //   }
        //   this.dataListLoading = false
        // })
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        if (id != null && id != "") {
          this.addOrUpdatePage(id)
        } else {
          this.$http({
            url: this.$http.adornUrl(`/innovate/innovatedeclarationprocesssetting/queryCount`),
            method: 'post',
            data: this.$http.adornData({
              'declareProcessName': 2,
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.addOrUpdatePage(id)
            } else {
              this.$message({
                message: data.msg,
                type: 'error',
                duration: 1500,
                onClose: () => {

                }
              })
            }
          })
        }

      },
      addOrUpdatePage(id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 新增 / 修改
      isUpdateHandle (id) {
        this.isUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.isUpdate.init(id)
        })
      },
      // 历史记录
      updateHistoryHandle (id) {
        this.isHistoyVisible = true
        this.$nextTick(() => {
          this.$refs.updateHistory.init(id)
        })
      },
      applyFinishIsVisible (item) {
        if (this.isAuth('innovate:project:apply:finish')) {
          if (item.projectFinishApplyStatus !== null || item.projectFinishApplyStatus !== '') {
            if (item.projectFinishApplyStatus === 0) {
              let roleIdList = this.$store.state.user.roleIdList
              for (let roleIndex = 0; roleIndex < roleIdList.length; roleIndex++) {
                if (roleIdList[roleIndex] === 2) {
                  return true
                }
              }
            }
          }
        }
        return false
      },
      // 详情
      detailHandle (id) {
        this.detailVisible = true
        this.$nextTick(() => {
          this.$refs.detail.init(id)
        })
      },
      // 审批
      applyFinishHandle (id) {
        this.$confirm('此操作将使该项目进入不可修改状态，并进入比赛审批流程，是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/innovate/finish/apply/apply'),
            method: 'post',
            params: this.$http.adornParams({
              'finishId': id,
              'apply': 'project_finish_apply_status',
              'roleId': 2
            }, false)
          }).then(({data}) => {
            this.$message({
              type: 'success',
              message: '提交成功!'
            })
            this.getDataList()
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消申请'
          })
        })
      },
      addOrUpadate (item) {
        if ((item.projectFinishApplyStatus === 0) &&
          this.isAuth('innovate:finish:update')) {
          return true
        }
      },
      isDelete (item) {
        if ((item.projectFinishApplyStatus === 0) &&
          this.isAuth('innovate:finish:delete')) {
          return true
        }
      },
      isUpadate (item) {
        if (item.projectStatus === 1) {
          return true
        }
      },
      // 删除
      deleteHandle (id) {
        var canDelete = true
        var matchIds = id ? [id] : this.dataListSelections.map(item => {
          if ((item.projectInfoEntity.projectFinishApplyStatus > 0) ||
            !this.isAuth('innovate:finish:delete')) {
            canDelete = false
          } else {
            canDelete = false
          }
          return item.projectInfoEntity.finishId
        })
        this.$confirm(`确定要进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if (canDelete) {
            this.$http({
              url: this.$http.adornUrl('/innovate/finish/info/delete'),
              method: 'post',
              data: this.$http.adornData(matchIds, false)
            }).then(({data}) => {
              if (data && data.code === 0) {
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
          } else {
            this.$message({
              message: '包含不可删除项目',
              type: 'error',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          }
        }).catch(() => {})
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
