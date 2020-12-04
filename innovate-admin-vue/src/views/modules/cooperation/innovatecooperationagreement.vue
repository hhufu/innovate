<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.enterpriseName" placeholder="企业名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('cooperation:innovatecooperationagreement:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('cooperation:innovatecooperationagreement:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
        <el-button v-if="isAuth('cooperation:innovatecooperationagreement:save')" type="primary" @click="exportAchieve()">导出</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      :default-sort="{prop: 'innovateCooperationAgreementEntity.enterpriseId', order: 'ascending'}"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="enterpriseId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="自增主键">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="enterpriseName"
        header-align="center"
        align="center"
        label="企业名称">
      </el-table-column>
      <el-table-column
        prop="instituteId"
        header-align="center"
        align="center"
        :formatter="fomatterInstitute"
        label="二级学院">
      </el-table-column>
      <el-table-column
        prop="agreementYear"
        header-align="center"
        align="center"
        :formatter="dateFormat"
        label="年度">
      </el-table-column>
      <el-table-column
        prop="agreementTime"
        header-align="center"
        align="center"
        label="协议时间">
        <template slot-scope="scope">
          {{parseTime(scope.row.agreementTime, "{y}-{m}-{d}")}}
        </template>
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="agreementMaterials"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="协议材料">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="enterpriseRecords"
        header-align="center"
        align="center"
        label="企业记录">
      </el-table-column>
      <el-table-column
        prop="userId"
        header-align="center"
        align="center"
        label="创建者">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="detailInfo(scope.row.enterpriseId)">查看</el-button>
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.enterpriseId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.enterpriseId)">删除</el-button>
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
  import AddOrUpdate from './innovatecooperationagreement-add-or-update'
  import DetailInfo from './agreement-detail-info'
  export default {
    data () {
      return {
        dataForm: {
          enterpriseName: ''
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
    activated () {
      this.getDataList()
      this.getInstituteName()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/cooperation/innovatecooperationagreement/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'enterpriseName': this.dataForm.enterpriseName,
            'isDel': 0
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
      // 查看
      detailInfo (id) {
        this.detailInfoVisible = true
        this.$nextTick(() => {
          this.$refs.detailInfo.init(id)
        })
      },
      // 格式化学院名称
      fomatterInstitute (e) {
        var actions = []
        Object.keys(this.instituteList).some((key) => {
          // eslint-disable-next-line eqeqeq
          if (this.instituteList[key].instituteId == parseInt(e.instituteId)) {
            actions.push(this.instituteList[key].instituteName)
            return true
          }
        })
        return actions.join('')
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.enterpriseId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/cooperation/innovatecooperationagreement/delete'),
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
      },
      // 导出
      exportAchieve () {
        this.dataListLoading = true
        var enterpriseIds = this.dataListSelections.map(item => {
          return item.enterpriseId
        })
        this.$http({
          url: this.$http.adornUrl('/cooperation/innovatecooperationagreement/export'),
          method: 'post',
          data: this.$http.adornData(enterpriseIds, false),
          responseType: 'blob'
        }).then((res) => {
          this.dataListLoading = false
          const blob = new Blob([res.data], {type: 'application/vnd.ms-excel'})
          let filename = '企业协议管理信息.xls'
          // 创建一个超链接，将文件流赋进去，然后实现这个超链接的单击事件
          const elink = document.createElement('a')
          elink.download = filename
          elink.style.display = 'none'
          elink.href = URL.createObjectURL(blob)
          document.body.appendChild(elink)
          elink.click()
          URL.revokeObjectURL(elink.href) // 释放URL 对象
          document.body.removeChild(elink)
        }).catch(() => {
          this.dataListLoading = false
          this.$message.error('导出失败!')
        })
      },
      // 时间格式化
      // 多选
      dateFormat (row, column) {
        var t = new Date(row.agreementYear)
        return t.getFullYear() + '年'
      }
    }
  }
</script>
