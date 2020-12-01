<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('profess:innovateprofessachieve:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('profess:innovateprofessachieve:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
<!--      <el-table-column-->
<!--        prop="professAchieveId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="自增主键">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="professUserId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="负责人id">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="awardAchieName"
        header-align="center"
        align="center"
        label="获奖名称">
      </el-table-column>
      <el-table-column
        prop="professAchieDirector"
        header-align="center"
        align="center"
        label="负责人">
      </el-table-column>
      <el-table-column
        prop="teamMenber"
        header-align="center"
        align="center"
        label="团队成员">
      </el-table-column>
      <el-table-column
        prop="professAchieveType"
        header-align="center"
        align="center"
        label="专创成果类型">
      </el-table-column>
      <el-table-column
        prop="instituteId"
        header-align="center"
        align="center"
        :formatter="fomatterInstitute"
        label="所属二级学院">
      </el-table-column>
      <el-table-column
        prop="awardTime"
        header-align="center"
        align="center"
        label="获得时间">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="professAchieveTypeId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="专创成果类型id">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="isDel"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="是否删除">-->
<!--      </el-table-column>-->
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.professAchieveId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.professAchieveId)">删除</el-button>
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
  </div>
</template>

<script>
  import AddOrUpdate from './innovateprofessachieve-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          key: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        instituteList: [] // 学院列表
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
      this.getInstituteName()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/profess/innovateprofessachieve/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key
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
      getInstituteName () {
        debugger
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/institute/all`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.instituteList = data.institute
          }
        })
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
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 格式化学院名称
      fomatterInstitute(e) {
        var actions = [];
        Object.keys(this.instituteList).some((key) => {
          if ( this.instituteList[key].instituteId == parseInt(e.instituteId)) {
            actions.push( this.instituteList[key].instituteName);
            return true;
          }
        })
        return actions.join('');
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.professAchieveId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/profess/innovateprofessachieve/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
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
        })
      }
    }
  }
</script>
