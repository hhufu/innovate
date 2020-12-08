<template>
  <el-dialog
    :title="!dataForm.trainingBaseId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="140px">
      <el-form-item label="实习实训基地名称" label-width="140px" prop="trainingBaseName">
        <el-input v-model="dataForm.trainingBaseName" placeholder="实习实训基地名称"></el-input>
      </el-form-item>
      <el-form-item label="所属二级学院" label-width="" prop="instituteId">
        <el-select v-model="dataForm.instituteId" placeholder="请选择二级学院">
          <el-option
            v-for="item in institus"
            :key="item.instituteId"
            :label="item.instituteName"
            :value="item.instituteId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="建立时间" prop="baseCreateTime">
        <el-date-picker
          v-model="dataForm.baseCreateTime"
          type="date"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="建立时间">
        </el-date-picker>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" :loading="loading" :disabled="loading">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data() {
      return {
        loading: false,
        visible: false,
        dataForm: {
          trainingBaseId: 0,
          trainingBaseName: '',
          instituteId: '',
          baseCreateTime: '',
          isDel: ''
        },
        dataRule: {
          trainingBaseName: [
            {required: true, message: '实习实训基地名称不能为空', trigger: 'blur'}
          ],
          instituteId: [
            {required: true, message: '所属二级学院不能为空', trigger: 'blur'}
          ],
          baseCreateTime: [
            {required: true, message: '建立时间不能为空', trigger: 'blur'}
          ]
        },
        institus: []
      }
    },
    methods: {
      init(id) {
        this.loading = false
        this.dataForm.trainingBaseId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.trainingBaseId) {
            this.$http({
              url: this.$http.adornUrl(`/training/innovatetrainingbaseinfo/info/${this.dataForm.trainingBaseId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.trainingBaseName = data.innovateTrainingBaseInfo.trainingBaseName
                this.dataForm.instituteId = data.innovateTrainingBaseInfo.instituteId
                this.dataForm.baseCreateTime = data.innovateTrainingBaseInfo.baseCreateTime
                this.dataForm.isDel = data.innovateTrainingBaseInfo.isDel
              }
            })
          }
          this.$http({
            url: this.$http.adornUrl('/innovate/sys/institute/all'),
            method: 'get',
          }).then(({data}) => {
            this.institus = data.institute
          })
        })
      },
      // 表单提交
      dataFormSubmit() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.loading = true
            this.$http({
              url: this.$http.adornUrl(`/training/innovatetrainingbaseinfo/${!this.dataForm.trainingBaseId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'trainingBaseId': this.dataForm.trainingBaseId || undefined,
                'trainingBaseName': this.dataForm.trainingBaseName,
                'instituteId': this.dataForm.instituteId,
                'baseCreateTime': this.dataForm.baseCreateTime,
                // 'isDel': this.dataForm.isDel
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
                this.loading = false
              }
            })
          }
        })
      }
    }
  }
</script>
