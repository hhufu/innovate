<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('points:innovatesyspoints:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('points:innovatesyspoints:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
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
      <table-tree-column
        prop="participateType"
        header-align="center"
        treeKey="integralId"
        width="320"
        label="申报类型">
      </table-tree-column>
      <el-table-column
        prop="raceGrade"
        header-align="center"
        align="center"
        label="项目名称">
      </el-table-column>
      <el-table-column
        prop="prizeGrade"
        header-align="center"
        align="center"
        label="奖项等级">
        <template slot-scope="scope">
          {{scope.row.prizeGrade? scope.row.prizeGrade:'-'}}
        </template>
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
        prop="integral"
        header-align="center"
        align="center"
        label="积分">
        <template slot-scope="scope">
          {{scope.row.integral?scope.row.integral +   '积分':''}}
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.integralId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.integralId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
<!--    <el-pagination-->
<!--      @size-change="sizeChangeHandle"-->
<!--      @current-change="currentChangeHandle"-->
<!--      :current-page="pageIndex"-->
<!--      :page-sizes="[10, 20, 50, 100]"-->
<!--      :page-size="pageSize"-->
<!--      :total="totalPage"-->
<!--      layout="total, sizes, prev, pager, next, jumper">-->
<!--    </el-pagination>-->
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './innovatesyspoints-add-or-update'
  import { treeDataTranslate } from '@/utils'
  import TableTreeColumn from '@/components/table-tree-column'

  export default {
    data () {
      return {
        dataForm: {
          key: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 100,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate,
      TableTreeColumn
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/points/innovatesyspoints/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'isDel': 0
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            // this.dataList = data.page.list
            this.dataList = treeDataTranslate(data.page.list, 'integralId')
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
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
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.integralId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/points/innovatesyspoints/delete'),
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
