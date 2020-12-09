<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.enterpriseName" placeholder="企业名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('enterprise:innovateenterpriseinfo:save')" type="primary" @click="exportLists()">导出
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
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="detailInfo(scope.row.settledEnterpId)">查看</el-button>
          <el-button type="text" size="small" @click="dialog(scope.row.settledEnterpId,9)">驳回</el-button>
          <el-button type="text" size="small" @click="hintVisible(scope.row.settledEnterpId,1)">通过</el-button>
          <el-button type="text" size="small" @click="dialog(scope.row.settledEnterpId,2)">不通过</el-button>
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
    <el-dialog
      :title="remarkFrom.applyStatus == 9 ? '驳回':'不通过'"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="800px">
      <el-form ref="remarkFrom" :model="remarkFrom" :rules="dataRule" @keyup.enter.native="dataFormSubmit()"
               label-width="120px">
        <el-form-item :label="remarkFrom.applyStatus == 9 ? '驳回原因':'不通过原因'" prop="remark">
          <el-input :rows="4" type="textarea" v-model="remarkFrom.remark"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="loading" :disabled="loading" @click="dataFormSubmit">确 定</el-button>
      </span>
    </el-dialog>
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
          enterpriseName: ''
        },
        remarkFrom: {
          remark: '',
          id: '',
          applyStatus: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        detailInfoVisible: false,
        apply_status: 0,
        dialogVisible: false,
        loading: false,
        dataRule: {
          remark: [
            {required: true, message: '原因不可为空', trigger: 'blur'}
          ]
        }
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
            enterpriseUserId: this.isAuth('enterprise:innovateenterpriseinfo:admin') ? null : this.$store.state.user.id,
            instituteId: this.isAuth('enterprise:innovateenterpriseinfo:superAdmin') ? null : this.$store.state.user.instituteId
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
        debugger
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
            instituteId: this.isAuth('enterprise:innovateenterpriseinfo:admin') ? this.$store.state.user.instituteId : null
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
      //提示确认框
      hintVisible(settledEnterpId, status) {
        this.$confirm(
          `确认后不可回退，确认提交吗？`,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        ).then(() => {
          this.applyStatus(settledEnterpId, status)
        })
      },
      // 表单提交
      dataFormSubmit() {
        this.$refs['remarkFrom'].validate((valid) => {
          if (valid) {
            this.loading = true
            this.$http({
              url: this.$http.adornUrl(
                `/enterprise/innovateenterpriseinfo/update`
              ),
              method: 'post',
              data: this.$http.adornData({
                infoEntity: {
                  settledEnterpId: this.remarkFrom.id,
                  applyStatus: this.remarkFrom.applyStatus,
                  remark: this.remarkFrom.remark
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
                    this.loading = false
                    this.dialogVisible = false
                    this.getDataList()
                  }
                })
              } else {
                this.loading = false
                this.$message.error(data.msg)
              }
            })
          }
        })
      },
      dialog(id, status) {
        if (this.remarkFrom.remark){
          this.$refs['remarkFrom'].resetFields()
        }
        this.dialogVisible = true
        this.remarkFrom.id = id
        this.remarkFrom.applyStatus = status
      }
    }
  }
</script>
