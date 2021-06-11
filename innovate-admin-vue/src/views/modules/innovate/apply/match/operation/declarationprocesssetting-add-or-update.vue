<template>
  <el-dialog
    :title="!dataForm.dpsId ? '新增申报流程' : '修改申报流程'"
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
      <el-col :span="24" v-if="dataForm.declareProcessName !== 3">
        <el-form-item label="评分规则" prop="reportSalesName">
          <el-upload
            class="upload-demo"
            ref="upload"
            :action="url"
            :data="{declareProcessName: dataForm.declareProcessName}"
            :on-success="successHandle"
            :on-remove="fileRemoveHandler"
            :on-exceed="fileExceed"
            :limit="1"
            :file-list="fileList">
            <el-button size="small" type="primary">点击上传</el-button>
            <span v-if="Object.keys(dataForm.attachName).length === 0" style="color: crimson">*请上传评分规则</span>
          </el-upload>
        </el-form-item>
      </el-col>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>

class DpsAttachment {
  // innovateDeclarationProcessSetting
  constructor (file) {
    this.name = file.attachName
    this.url = file.attachPath
    this.file = file
  }
}

  export default {
    data () {
      return {
        url: '',
        visible: false,
        fileList: [],
        dataForm: {
          dpsId: 0,
          declareProcessName: 1,
          startTime: '',
          endTime: '',
          remark: '',
          dateTimeRange: [],
          attachName: '',
          attachPath: '',
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
        }, {
          value: 3,
          label: '创新创业实践积分申请'
        }]
      }
    },
    methods: {
      init (id) {
        this.url = this.$http.adornUrl(`/innovate/innovatedeclarationprocesssetting/upload?token=${this.$cookie.get('token')}`)
        this.dataForm.dpsId = id || 0
        if (id !== null){
          this.dataForm.attachPath = ''
          this.dataForm.attachName = ''
        }
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          this.$refs.upload.clearFiles()
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
                this.dataForm.attachName = data.innovateDeclarationProcessSetting.attachName
                this.dataForm.attachPath = data.innovateDeclarationProcessSetting.attachPath
                if (data.innovateDeclarationProcessSetting.attachName !== null) {
                  var tempFile = []
                  tempFile.push(new DpsAttachment(data.innovateDeclarationProcessSetting))
                  this.fileList = tempFile
                }
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (this.dataForm.attachPath === '' || this.dataForm.attachName === '') {
            this.$message.error('请上传评分规则')
            return
          }
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/innovate/innovatedeclarationprocesssetting/${!this.dataForm.dpsId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'dpsId': this.dataForm.dpsId || undefined,
                'declareProcessName': this.dataForm.declareProcessName,
                'startTime': this.dataForm.dateTimeRange[0],
                'endTime': this.dataForm.dateTimeRange[1],
                'remark': this.dataForm.remark,
                'attachName': this.dataForm.attachName,
                'attachPath': this.dataForm.attachPath,
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
      },
      // 上传成功
      successHandle (response, file, fileList) {
        if (response && response.code === 0) {
          this.$message({
            showClose: true,
            message: '文件上传成功',
            type: 'success'
          });
          this.dataForm.attachName = response.innovateDeclarationProcessSetting.attachName
          this.dataForm.attachPath = response.innovateDeclarationProcessSetting.attachPath
        } else {
          this.$message.error(response.msg)
        }
      },
      fileRemoveHandler (file, fileList) {
        this.dataForm.attachName = ''
        this.dataForm.attachPath = ''
      },
      fileExceed (files, fileList) {
        this.$message.error('文件超出个数限制')
      }
    }
  }
</script>
