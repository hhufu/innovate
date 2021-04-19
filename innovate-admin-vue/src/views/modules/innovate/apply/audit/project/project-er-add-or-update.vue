<template>
  <el-dialog
    width="60%"
    @close="closeDialog"
    v-loading="dataListLoading"
    :title="!dataForm.declareId ? '新增大创信息' : '修改大创信息'"
    :close-on-click-modal="false"
    :visible.sync="visible">
  <el-form :model="dataForm" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="16rem" style="width: 94%; margin: 0 auto;height: 150px">
    <el-col :span="24">
      <el-form-item label="所属二级学院" prop="declareName">
        <el-select v-model="dataForm.instituteId" placeholder="请选择二级学院" clearable>
          <el-option
            v-for="inst in instituteList"
            :key="inst.instituteName"
            :label="inst.instituteName"
            :value="inst.instituteId">
          </el-option>
        </el-select>
      </el-form-item>
    </el-col>
  </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" :loading="addLoading">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
    export default {
      name: "project-er-add-or-update",
      data() {
        return {
          visible: false,
          instituteList: this.$store.state.user.institute,
          dataListLoading: false,
          dataForm: {
            declareId: '',
            instituteId: 0
          },
          addLoading: false
        }
      },
      methods: {
        init(row) {
          this.visible = true
          this.dataForm.declareId = row.declareId
          this.dataForm.instituteId = row.instituteId
        },
        closeDialog () {
          this.visible = false
          this.$emit('refreshDataList')
        },
        // 表单提交
        dataFormSubmit () {
            this.dataForm.declareId = this.dataForm.declareId || undefined

              this.$confirm(`确定要修改该项目所属二级学院吗`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                this.dataForm.isUpdate = 0
                this.addLoading = true
                this.$http({
                  url: this.$http.adornUrl(`/innovate/declare/info/update`),
                  method: 'post',
                  data: this.$http.adornData({
                    'declareInfoEntity': this.dataForm,
                    'userPersonInfoEntities': [],
                    'declareTeacherEntities': [],
                    'declareAttachEntities': [],
                    'declareStaffInfoEntities': [],
                    'declareAwardEntities': [],
                    'delAttachLists': []
                  })
                }).then(({data}) => {
                  if (data && data.code === 0) {
                    this.$message({
                      message: '操作成功',
                      type: 'success',
                      duration: 1500,
                      onClose: () => {
                        this.visible = false
                        this.addLoading = false
                        this.$emit('refreshDataList')
                      }
                    })
                  } else {
                    this.$message.error(data.msg)
                    this.addLoading = true
                  }
                })
              }).catch(() => {})

        },
      }

    }
</script>

<style scoped>

</style>
