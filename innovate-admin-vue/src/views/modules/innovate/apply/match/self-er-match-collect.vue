<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-select v-model="dataForm.instituteId" placeholder="请选择二级学院" disabled="">
          <el-option
            v-for="inst in instituteList"
            :key="inst.instituteName"
            :label="inst.instituteName"
            :value="inst.instituteId">
          </el-option>
        </el-select>
        <el-form-item label="项目处于">
          <el-select v-model="processStatus" placeholder="请选择审核状态">
            <el-option
              v-for="item in processStatusList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="项目状态">
          <el-select v-model="noPass" placeholder="请选择状态" clearable>
            <el-option
              v-for="item in noPassList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-button v-if="isAuth('sys:user:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <!--年度 start-->
        <el-date-picker
          v-model="dataForm.matchTime"
          align="right"
          type="year"
          placeholder="请选择年度">
        </el-date-picker>
        <!--年度 end-->
        <el-button @click="getDataList()">查询</el-button>
        <el-button type="primary" @click="collectDetail(dataForm.instituteId,dataForm.matchTime, processStatus, noPass)">导出</el-button>
      </el-form-item>
    </el-form>
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
        width="80"
        label="序号">
      </el-table-column>
      <el-table-column
        prop="matchInfoEntity.matchName"
        header-align="center"
        align="center"
        width="200"
        label="项目名称">
      </el-table-column>
      <el-table-column
        prop="matchInfoEntity.matchTeamName"
        header-align="center"
        align="center"
        width="100"
        label="团队名称">
      </el-table-column>
      <el-table-column
        prop="userPersonInfoEntities"
        header-align="center"
        align="center"
        width="70"
        label="负责人">
        <template slot-scope="scope">
          <span v-for="user in scope.row.userPersonInfoEntities" v-text="user.sysUserEntity.name"></span>
        </template>
      </el-table-column>
      <el-table-column
        prop="matchInfoEntity"
        header-align="center"
        align="center"
        label="申报类型">
        <template slot-scope="scope">
          <span v-for="type in matchTypeList" v-if="type.value === scope.row.matchInfoEntity.matchType" v-text="type.label"></span>
        </template>
      </el-table-column>
      <el-table-column
        prop="matchTypeList"
        header-align="center"
        align="center"
        label="申报组别">
        <template slot-scope="scope">
          <span v-for="type in matchGroupTypeList" v-if="type.value === scope.row.matchInfoEntity.matchGroupType" v-text="type.label"></span>
        </template>
      </el-table-column>
      <el-table-column
        prop="matchStaffInfoEntities.length"
        header-align="center"
        align="center"
        width="60"
        label="团队人数">
      </el-table-column>
      <el-table-column
        prop="matchStaffInfoEntities"
        header-align="center"
        align="center"
        label="团队成员姓名">
        <template slot-scope="scope">
          <span v-for="staff in scope.row.matchStaffInfoEntities" v-text="staff.staffName+' '"></span>
        </template>
      </el-table-column>
      <el-table-column
        prop="userTeacherInfoEntities"
        header-align="center"
        align="center"
        label="指导老师">
        <template slot-scope="scope">
          <span v-for="teacher in scope.row.userTeacherInfoEntities" v-text="teacher.sysUserEntity.name+' '"></span>
        </template>
      </el-table-column>
      <el-table-column
        prop="matchInfoEntity.projectAuditApplyStatus"
        header-align="center"
        :formatter="formatterProcess"
        align="center"
        label="审核状态">
      </el-table-column>
      <el-table-column
        prop="matchInfoEntity.matchScoreAvg"
        header-align="center"
        align="center"
        width="80"
        label="平均得分">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="80"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="openMatchInfoDetail(scope.row.matchInfoEntity.matchId)">查看</el-button>
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
    <self-er-match-collect-detail v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></self-er-match-collect-detail>
    <match-info-detail v-if="matchInfoDetailVisible" ref="detail" @refreshDataList="getDataList"></match-info-detail>
  </div>
</template>

<script>
  import SelfErMatchCollectDetail from './operation/self-er-match-collect-detail'
  import MatchInfoDetail from './operation/info-detail'

  export default {
    data () {
      return {
        dataForm: {
          userName: '',
          matchTime: new Date(),
          instituteId: this.$store.state.user.instituteId,
          institute: {}
        },
        noPass:'0',
        noPassList: [{
          label: '正常',
          value: '0'
        },{
          label: '已退回',
          value: '1'
        },{
          label: '全部',
          value: ''
        }],
        processStatus: 1,
        processStatusList: [{
          label: '项目负责人提交',
          value:1
        }, {
          label: '指导老师审批',
          value: 2
        }, {
          label: '二级学院审批',
          value: 3
        }, {
          label: '管理员分配评委组',
          value: 4
        }, {
          label: '评委审批',
          value: 5
        }, {
          label: '管理员审批',
          value: 6
        },{
          label: '全部状态',
          value: 8
        }
        ],// 流程下拉列表
        dataList: [],
        pageIndex: 1,
        currPage: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        matchInfoDetailVisible: false,
        addOrUpdateVisible: false,
        matchYear: '',
        matchYears: [],
        instituteList: this.$store.state.user.institute,
        matchTypeList: [
          {value: 1, label: '论文、调研报告'}, {value: 2, label: '视频'},
          {value: 3, label: '设计作品'}, {value: 4, label: '创业计划书'},
          {value: 5, label: '创新创业实践项目'}, {value: 6, label: '其它'}
        ],
        matchGroupTypeList: [
          {value: 1, label: '创意组'}, {value: 2, label: '初创组'},
          {value: 3, label: '成长组'}, {value: 4, label: '师生共创组'},
          {value: 5, label: '"青年红色梦之旅"赛道'}, {value: 6, label: '其它'}
        ]
      }
    },
    components: {
      SelfErMatchCollectDetail,
      MatchInfoDetail
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.$http({
          url: this.$http.adornUrl(`/innovate/match/info/list`),
          method: 'get',
          params: this.$http.adornParams({
            'instituteId': this.dataForm.instituteId,
            'matchTime': this.dataForm.matchTime == null ? '' : this.dataForm.matchTime.getFullYear(),
            'currPage': this.pageIndex,
            'pageSize': this.pageSize,
            'hasApply': '1',
            'apply': this.processStatus === 8 ? null : 'project_match_apply_status',
            'applyStatus': this.processStatus === 8 ? null: this.processStatus,
            'project_match_apply_status_more': this.processStatus === 8 ? 0: null,// 审核状态大于0,
            'noPassStatus': this.noPass !== '' ? parseInt(this.noPass) : 0,
            'noPass': this.noPass !== '' ? 'match_no_pass' : '',
            'isEr': true,
            'isDel': 0
          })
        }).then(({data}) => {
          console.log(data)
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
            this.dataListLoading = false
          } else {
            this.dataList = []
            this.totalPage = 0
            this.$message.error(data.msg)
            this.dataListLoading = false
          }
        })
      },
      // 转换流程进度名称
      formatterProcess(row) {
        var arr = ["项目负责人提交", "指导老师审批", "二级学院审批", "管理员分配评委组", "评委审批", "管理员审批"]
        if (row.matchInfoEntity.matchNoPass == 1)
          return "已退回（" + arr[row.matchInfoEntity.projectMatchApplyStatus - 1] + "）"
        return arr[row.matchInfoEntity.projectMatchApplyStatus - 1]
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
      collectDetail (id, time, processStatus, noPass) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id, time, processStatus, noPass)
        })
      },
      // 详情
      openMatchInfoDetail (id) {
        this.matchInfoDetailVisible = true
        this.$nextTick(() => {
          this.$refs.detail.init(id)
        })
      }
    }
  }
</script>
