<template>
  <el-dialog
    :title="!dataForm.id ? '新增申报流程' : '修改申报流程'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="150px">
    <el-form-item label="申报流程名称" prop="declareProcessName">
      <el-select v-model="dataForm.declareProcessName" placeholder="请选择申报流程名称">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
    </el-form-item>
      <el-form-item label="申报时间范围" prop="dateTimeRange">
        <el-date-picker
          v-model="dataForm.dateTimeRange"
          type="datetimerange"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          align="right">
        </el-date-picker>
      </el-form-item>
    <el-form-item label="备注" prop="">
      <el-input v-model="dataForm.remark" type="textarea" placeholder="备注"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          dpsId: 0,
          declareProcessName: '',
          startTime: '',
          endTime: '',
          remark: '',
          dateTimeRange: []
        },
        dataRule: {
          declareProcessName: [
            { required: true, message: '申报流程名称不能为空', trigger: 'blur' }
          ],
          dateTimeRange: [
            { required: true, message: '申报时间范围不能为空', trigger: 'blur' }
          ],
          endTime: [
            { required: true, message: '截止时间不能为空', trigger: 'blur' }
          ],
          remark: [
            { required: true, message: '备注不能为空', trigger: 'blur' }
          ]
        },
        options: [{
          value: 1,
          label: '大创项目立项'
        }, {
          value: 2,
          label: '大创项目结题'
        }]
      }
    },
    methods: {
      init (id) {
        this.dataForm.dpsId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.dpsId) {
            this.$http({
              url: this.$http.adornUrl(`/innovate/innovatedeclarationprocesssetting/info/${this.dataForm.dpsId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.declareProcessName = data.innovateDeclarationProcessSetting.declareProcessName
                this.dataForm.startTime = data.innovateDeclarationProcessSetting.startTime
                this.dataForm.endTime = data.innovateDeclarationProcessSetting.endTime
                this.dataForm.remark = data.innovateDeclarationProcessSetting.remark
                this.dataForm.dateTimeRange = [this.dataForm.startTime, this.dataForm.endTime]
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/innovate/innovatedeclarationprocesssetting/${!this.dataForm.dpsId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'dpsId': this.dataForm.dpsId || undefined,
                'declareProcessName': this.dataForm.declareProcessName,
                'startTime': this.dataForm.dateTimeRange[0],
                'endTime': this.dataForm.dateTimeRange[1],
                'remark': this.dataForm.remark
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
              }
            })
          }
        })
      }
    }
  }
</script>
