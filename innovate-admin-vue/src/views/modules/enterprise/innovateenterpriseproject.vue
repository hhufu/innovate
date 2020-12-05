<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-date-picker
          v-model="dataForm.projectYear"
          @change="getDataList"
          align="right"
          type="year"
          placeholder="请选择年度">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.projectName" placeholder="项目名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('enterprise:innovateenterpriseproject:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('enterprise:innovateenterpriseproject:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
        <el-button v-if="isAuth('enterprise:innovateenterpriseproject:save')" type="primary" @click="exportLists()">导出</el-button>
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
        label="序号">
      </el-table-column>
      <el-table-column
        prop="enterpriseName"
        header-align="center"
        align="center"
        label="企业名称">
      </el-table-column>
      <el-table-column
        prop="projectName"
        header-align="center"
        align="center"
        label="项目名称">
      </el-table-column>
      <el-table-column
        prop="projStartTime"
        header-align="center"
        align="center"
        label="项目开始时间">
        <template slot-scope="scope">
          {{parseTime(scope.row.projStartTime, "{y}年{m}月{d}日")}}
        </template>
      </el-table-column>
      <el-table-column
        prop="projStopTime"
        header-align="center"
        align="center"
        label="截止时间">
        <template slot-scope="scope">
          {{parseTime(scope.row.projStopTime, "{y}年{m}月{d}日")}}
        </template>
      </el-table-column>
      <el-table-column
        prop="projectYear"
        header-align="center"
        align="center"
        label="项目年度">
      </el-table-column>
      <el-table-column
        prop="projectDirector"
        header-align="center"
        align="center"
        label="项目负责人">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="detailInfo(scope.row.enterpProjId)">查看</el-button>
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.enterpProjId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.enterpProjId)">删除</el-button>
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
import AddOrUpdate from './innovateenterpriseproject-add-or-update'
import DetailInfo from './enterpriseprojects-detail'
export default {
  data () {
    return {
      dataForm: {
        projectName: '',
        projectYear:null
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
    this.getDataList();
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("/enterprise/innovateenterpriseproject/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          project_name: this.dataForm.projectName,
          projectYear: this.dataForm.projectYear== null ? null : this.dataForm.projectYear,
          enterpriseUserId: this.isAuth('enterprise:innovateenterpriseinfo:superAdmin') ? null : this.$store.state.user.id,
          instituteId: this.isAuth('enterprise:innovateenterpriseinfo:admin') ? this.$store.state.user.instituteId : null
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list;
          this.totalPage = data.page.totalCount;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    // 查看
    detailInfo (id) {
      this.detailInfoVisible = true
      this.$nextTick(() => {
        this.$refs.detailInfo.init(id)
      })
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    // 删除
    deleteHandle(id) {
      var ids = id
        ? [id]
        : this.dataListSelections.map(item => {
          return item.enterpProjId;
          });
      this.$confirm(
        `确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        this.$http({
          url: this.$http.adornUrl(
            "/enterprise/innovateenterpriseproject/delete"
          ),
          method: "post",
          data: this.$http.adornData(ids, false)
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.getDataList();
              }
            });
          } else {
            this.$message.error(data.msg);
          }
        });
      });
    },
    // 导出
    exportLists() {
      this.dataListLoading = true;
      var trainBaseIds = this.dataListSelections.map(item => {
        return item.enterpProjId;
      });
      this.$http({
        url: this.$http.adornUrl(
          "/enterprise/innovateenterpriseproject/export"
        ),
        method: "post",
        data: this.$http.adornData(trainBaseIds, false),
        responseType: "blob"
      })
        .then(res => {
          this.dataListLoading = false;
          const blob = new Blob([res.data], {
            type: "application/vnd.ms-excel"
          });
          let filename = "企业项目.xls";
          // 创建一个超链接，将文件流赋进去，然后实现这个超链接的单击事件
          const elink = document.createElement("a");
          elink.download = filename;
          elink.style.display = "none";
          elink.href = URL.createObjectURL(blob);
          document.body.appendChild(elink);
          elink.click();
          URL.revokeObjectURL(elink.href); // 释放URL 对象
          document.body.removeChild(elink);
        })
        .catch(() => {
          this.dataListLoading = false;
          this.$message.error("导出失败!");
        });
    }
  }
};
</script>
