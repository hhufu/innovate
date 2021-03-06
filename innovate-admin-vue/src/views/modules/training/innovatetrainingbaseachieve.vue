<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-date-picker
          @change="getDataList"
          v-model="dataForm.materialYear"
          align="right"
          type="year"
          placeholder="请选择年度">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.trainingBaseName" placeholder="实训基地名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('training:innovatetrainingbaseachieve:save')" type="primary" @click="addOrUpdateHandle(1)">新增</el-button>
        <el-button v-if="isAuth('training:innovatetrainingbaseachieve:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
        <el-button v-if="isAuth('training:innovatetrainingbaseachieve:save')" type="primary" @click="exportAchieve()">导出</el-button>
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
        prop=""
        type="index"
        header-align="center"
        align="center"
        label="ID">
      </el-table-column>
      <el-table-column
        prop="trainingBaseName"
        header-align="center"
        align="center"
        label="实训基地名称">
      </el-table-column>
      <el-table-column
        prop="materialYear"
        header-align="center"
        align="center"
        :formatter="dateFormat"
        label="材料年度">
      </el-table-column>
      <el-table-column
        prop="materialType"
        header-align="center"
        align="center"
        label="成果类型">
      </el-table-column>
      <el-table-column
        prop="instituteId"
        header-align="center"
        align="center"
        :formatter="fomatterInstitute"
        label="所属二级学院">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="detailInfo(scope.row.trainingAchieveId)">查看</el-button>
          <el-button type="text" size="small" @click="addOrUpdateHandle(2, scope.row.trainingAchieveId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row)">删除</el-button>
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
    <detail-info v-if="detailInfoVisible" ref="detailInfo"></detail-info>
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './innovatetrainingbaseachieve-add-or-update'
  import DetailInfo from './achieve-detail-info'
  export default {
    data () {
      return {
        dataForm: {
          trainingBaseName: '',
          materialYear: new Date()
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
          url: this.$http.adornUrl('/training/innovatetrainingbaseachieve/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'isDel': 0,
            'materialYear': this.dataForm.materialYear == null ? null : this.dataForm.materialYear.getFullYear(),
            'trainingBaseName': this.dataForm.trainingBaseName,
            'instituteId': this.isAuth("training:export:admin") === true ? null : this.$store.state.user.instituteId
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
      // 查询学院列表
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
      // 查看
      detailInfo (id) {
        this.detailInfoVisible = true
        this.$nextTick(() => {
          this.$refs.detailInfo.init(id)
        })
      },
      // 新增(index==1) / 修改(index==2)
      addOrUpdateHandle (index, id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(index, id)
        })
      },
      // 删除
      deleteHandle (row) {
        var ids = (row ?true : false) ? [row.trainingAchieveId] : this.dataListSelections.map(item => {
          return item.trainingAchieveId
        })
        var name = (row ?true : false) ? [row.trainingBaseName] : this.dataListSelections.map(item => {
          return item.trainingBaseName
        })
        this.$confirm(`确定对[实训基地名称=${name.join(',')}]进行[${(row ? row.trainingAchieveId : false) ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/training/innovatetrainingbaseachieve/delete'),
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
      exportAchieve () {
        if (this.dataList.length > 0) {
          this.dataListLoading = true
          var ids = this.dataListSelections.map(item => {
            return item.trainingAchieveId
          })
          let materialYear = this.dataForm.materialYear == null ? null : this.dataForm.materialYear.getFullYear()
          let dataForm = {
            ids: ids,
            instituteId: this.isAuth("training:export:admin") === true ? null : this.$store.state.user.instituteId,
            materialYear: materialYear,
            trainingBaseName: this.dataForm.trainingBaseName
          }
          this.$http({
            url: this.$http.adornUrl('/training/innovatetrainingbaseachieve/export'),
            method: 'post',
            data: this.$http.adornData(dataForm, false),
            responseType: 'blob'
          }).then((res) => {
            this.dataListLoading = false
            const blob = new Blob([res.data], {type: 'application/vnd.ms-excel'})
            let filename = '实训基地成果.xls'
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
        } else{
          this.$message.error('无导出数据');
        }
      },
      // 时间格式化
      // 多选
      dateFormat (row, column) {
        var t = new Date(row.materialYear)
        return t.getFullYear() + '年'
      },
    }
  }
</script>
