<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-date-picker
          @change="getDataList"
          v-model="dataForm.settledTime"
          align="right"
          clearable
          type="year"
          placeholder="请选择年度">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.enterpriseName" placeholder="企业名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('enterprise:innovateenterpriseinfo:save')" type="primary" @click="addOrUpdateHandle()">
          新增
        </el-button>
        <el-button v-if="isAuth('enterprise:innovateenterpriseinfo:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
        <el-button v-if="isAuth('enterprise:innovateenterpriseinfo:save')" type="primary" @click="exportLists()">导出
        </el-button>
      </el-form-item>
    </el-form>
    <el-card>
      <el-radio-group v-model="apply_status" @change="getDataList">
        <el-radio :label="9" v-if="isAuth('enterprise:innovateenterpriseinfo:toSubmit')">待提交</el-radio>
        <el-radio :label="0">审核中</el-radio>
        <el-radio :label="1">已通过</el-radio>
        <el-radio :label="2">未通过</el-radio>
      </el-radio-group>
    </el-card>
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
        prop="enterpriseName"
        header-align="center"
        align="center"
        label="企业名称">
      </el-table-column>
      <el-table-column
        prop="enterpriseDirector"
        header-align="center"
        align="center"
        label="企业负责人">
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
        <template slot-scope="scope">
          {{parseTime(scope.row.settledTime, "{y}-{m}-{d}")}}
        </template>
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
        prop="remark"
        header-align="center"
        align="center"
        label="备注"
        v-if="apply_status=== 9 || apply_status=== 2">
        <template slot-scope="scope">
          {{scope.row.remark || '无'}}
        </template>
      </el-table-column>

      <el-table-column
        prop="applyStatus"
        header-align="center"
        align="center"
        label="审核状态">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.applyStatus === '9'"
                  disable-transitions>待提交
          </el-tag>
          <el-tag type="primary" v-if="scope.row.applyStatus === '0'"
                  disable-transitions>审核中
          </el-tag>
          <el-tag type="success" v-if="scope.row.applyStatus === '1'"
                  disable-transitions>已通过
          </el-tag>
          <el-tag type="danger" v-if="scope.row.applyStatus === '2'"
                  disable-transitions>未通过
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" v-if="scope.row.applyStatus === '9'"
                     @click="hintVisible(scope.row.settledEnterpId)">提交审核
          </el-button>
          <el-button type="text" size="small" @click="detailInfo(scope.row.settledEnterpId)">查看</el-button>
          <el-button type="text" v-if="apply_status ===0 ||apply_status ===9  " size="small"
                     @click="addOrUpdateHandle(scope.row.settledEnterpId)">修改
          </el-button>
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
    <detail-info v-if="detailInfoVisible" ref="detailInfo"></detail-info>
  </div>
</template>

<script>
  import AddOrUpdate from './innovateenterpriseinfo-add-or-update'
  import DetailInfo from './enterpriseinfo-dateil'

  export default {
    data() {
      return {
        dataForm: {
          enterpriseName: '',
          settledTime: new Date() // 默认当前时间年度
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        detailInfoVisible: false,
        apply_status: 1
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
        this.dataListLoading = true;
        this.$http({
          url: this.$http.adornUrl("/enterprise/innovateenterpriseinfo/list"),
          method: "get",
          params: this.$http.adornParams({
            page: this.pageIndex,
            limit: this.pageSize,
            enterpriseName: this.dataForm.enterpriseName,
            apply_status: this.apply_status,
            enterpriseUserId: this.isAuth('enterprise:innovateenterpriseinfo:superAdmin') ? null : this.$store.state.user.id,
            instituteId: this.isAuth('enterprise:innovateenterpriseinfo:admin') ? this.$store.state.user.instituteId : null,
            settledTime: this.dataForm.settledTime == null ? null : this.dataForm.settledTime.getFullYear()
          })
        }).then(({data}) => {
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
      detailInfo(id) {
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
            return item.settledEnterpId;
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
            url: this.$http.adornUrl("/enterprise/innovateenterpriseinfo/delete"),
            method: "post",
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
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
        if (this.dataList.length > 0) {
          this.dataListLoading = true;
          var trainBaseIds = this.dataListSelections.map(item => {
            return item.settledEnterpId;
          });
          this.$http({
            url: this.$http.adornUrl("/enterprise/innovateenterpriseinfo/export"),
            method: "post",
            data: this.$http.adornData(trainBaseIds, false),
            params: this.$http.adornParams({
              apply_status: this.apply_status,
              enterpriseName: this.dataForm.enterpriseName,
              enterpriseUserId: this.isAuth('enterprise:innovateenterpriseinfo:superAdmin') ? null : this.$store.state.user.id,
              instituteId: this.isAuth('enterprise:innovateenterpriseinfo:admin') ? this.$store.state.user.instituteId : null,
              settledTime: this.dataForm.settledTime == null ? null : this.dataForm.settledTime.getFullYear()
            }),
            responseType: "blob"
          })
            .then(res => {
              this.dataListLoading = false;
              const blob = new Blob([res.data], {
                type: "application/vnd.ms-excel"
              });
              let filename = "入驻企业.xls";
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
        } else {
          this.$message.error('无导出数据');
        }
      },
      // 状态审核
      applyStatus(settledEnterpId, status) {
        this.$http({
          url: this.$http.adornUrl(
            `/enterprise/innovateenterpriseinfo/update`
          ),
          method: 'post',
          data: this.$http.adornData({
            infoEntity: {
              settledEnterpId: settledEnterpId,
              applyStatus: status
            },
            attachEntities: []
          })
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
      },
      hintVisible(settledEnterpId) {
        this.$confirm(
          `确认后不可回退，确认提交吗？`,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        ).then(() => {
          this.applyStatus(settledEnterpId, 0)
        })
      }
    }
  }
</script>
