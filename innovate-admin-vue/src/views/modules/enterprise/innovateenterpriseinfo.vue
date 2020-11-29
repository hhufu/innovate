<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('enterprise:innovateenterpriseinfo:save')" type="primary" @click="addOrUpdateHandle()">
          新增
        </el-button>
        <el-button v-if="isAuth('enterprise:innovateenterpriseinfo:delete')" type="danger" @click="deleteHandle()"
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
        prop="settledEnterpId"
        header-align="center"
        align="center"
        label="自增主键（入驻企业id）">
      </el-table-column>
      <el-table-column
        prop="enterpriseName"
        header-align="center"
        align="center"
        label="企业名称">
      </el-table-column>
      <el-table-column
        prop="enterpriseUserId"
        header-align="center"
        align="center"
        label="用户ID">
      </el-table-column>
      <el-table-column
        prop="enterpriseDirector"
        header-align="center"
        align="center"
        label="企业负责人姓名">
      </el-table-column>
      <el-table-column
        prop="departmentDirector"
        header-align="center"
        align="center"
        label="负责人所在学院">
      </el-table-column>
      <el-table-column
        prop="settledTime"
        header-align="center"
        align="center"
        label="入驻时间">
      </el-table-column>
      <el-table-column
        prop="enterpriseType"
        header-align="center"
        align="center"
        label="企业类型">
      </el-table-column>
      <el-table-column
        prop="businessScope"
        header-align="center"
        align="center"
        label="主要经营范围">
      </el-table-column>

      <el-table-column
        prop="applyStatus"
        header-align="center"
        align="center"
        label="审核状态">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.applyStatus === '0'"
            disable-transitions>未审核
          </el-tag>
          <el-tag type="success" v-if="scope.row.applyStatus === '1'"
                  disable-transitions>已通过
          </el-tag>
          <el-tag type="danger" v-if="scope.row.applyStatus === '2'"
                  disable-transitions>未通过
          </el-tag>
        </template>
      </el-table-column>

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
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.settledEnterpId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.settledEnterpId)">删除</el-button>
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
  import AddOrUpdate from './innovateenterpriseinfo-add-or-update'

  export default {
    data() {
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
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    activated() {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList() {
        // debugger
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/enterprise/innovateenterpriseinfo/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.dataForm.key,
            'order': 'apply_status'
          })
        }).then(({data}) => {
          console.log(data)
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
      // 删除
      deleteHandle(id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.settledEnterpId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/enterprise/innovateenterpriseinfo/delete'),
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
