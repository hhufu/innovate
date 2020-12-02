<template>
  <el-dialog
    :title="!dataForm.materialsId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="模糊id " prop="functionId">
      <el-input v-model="dataForm.functionId" placeholder="模糊id "></el-input>
    </el-form-item>
    <el-form-item label="附件名称" prop="attachName">
      <el-input v-model="dataForm.attachName" placeholder="附件名称"></el-input>
    </el-form-item>
    <el-form-item label="附件路径" prop="attachPath">
      <el-input v-model="dataForm.attachPath" placeholder="附件路径"></el-input>
    </el-form-item>
    <el-form-item label="上传时间" prop="attachTime">
<!--      <el-input v-model="dataForm.attachTime" placeholder="上传时间"></el-input>-->
      <el-date-picker
        v-model="dataForm.attachTime"
        type="date"
        format="yyyy-MM-dd"
        placeholder="选择日期">
      </el-date-picker>
    </el-form-item>
      <el-col :span="24">
        <el-form-item label="附件要求">
          <template>
            <el-alert
              title=""
              type="success"
              :closable="false"
              :description="fileAskContent">
            </el-alert>
          </template>
        </el-form-item>
      </el-col>
      <!--独立附件start-->
      <el-col :span="24">
        <el-form-item label="相关附件" prop="reportSalesName">
          <el-upload
            class="upload-demo"
            :action="url"
            :data="{functionId: dataForm.functionId}"
            :on-success="successHandle1"
            :on-remove="fileRemoveHandler"
            :file-list="fileList">
            <el-button size="small" type="primary">点击上传</el-button>
            <span v-if="fileIsNull" style="color: crimson">*请上传相关附件</span>
          </el-upload>
        </el-form-item>
      </el-col>
<!--    <el-form-item label="是否删除" prop="isDel">-->
<!--      <el-input v-model="dataForm.isDel" placeholder="是否删除"></el-input>-->
<!--    </el-form-item>-->
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  class Cooperationachment {
    constructor (file) {
      this.name = file.attachName
      this.url = file.attachPath
      this.file = file
    }
  }
  export default {
    data () {
      return {
        visible: false,
        url: '',
        fileAskContent: '无',
        fileIsNull: false,
        fileList: [],
        attachLists: [],
        dataForm: {
          materialsId: 0,
          functionId: '',
          attachName: '',
          attachPath: '',
          attachTime: '',
          isDel: ''
        },
        dataRule: {
          functionId: [
            { required: true, message: '模糊id不能为空', trigger: 'blur' }
          ],
          attachName: [
            { required: true, message: '附件名称不能为空', trigger: 'blur' }
          ],
          attachPath: [
            { required: true, message: '附件路径不能为空', trigger: 'blur' }
          ],
          attachTime: [
            { required: true, message: '上传时间不能为空', trigger: 'blur' }
          ],
          isDel: [
            { required: true, message: '是否删除不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.url = this.$http.adornUrl(`/cooperation/innovatecooperationmaterials/upload?token=${this.$cookie.get('token')}`)
        this.dataForm.materialsId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.materialsId) {
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovatecooperationmaterials/info/${this.dataForm.materialsId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                // this.dataForm = data.innovateCooperationMaterials
                this.dataForm.functionId = data.innovateCooperationMaterials.functionId
                this.dataForm.attachName = data.innovateCooperationMaterials.attachName
                this.dataForm.attachPath = data.innovateCooperationMaterials.attachPath
                this.dataForm.attachTime = data.innovateCooperationMaterials.attachTime
                this.dataForm.isDel = data.innovateCooperationMaterials.isDel
                this.attchLists = data.innovateCooperationMaterials.innovateCooperationMaterialsEntities
                // 附件回显
                let tempFinishAtta = []
                for (var i = 0; i < this.attachLists.length; i++) {
                  tempFinishAtta.push(new Cooperationachment(this.attachLists[i]))
                }
                this.fileList = tempFinishAtta
                this.isTeacherInfoNull()
                this.dataListLoading = false
              }
            })
          }
          // 获取文件要求：类型=>1 大创,2 中期检查,3 赛事,4 结题
          this.dataListLoading = true
          this.$http({
            url: this.$http.adornUrl(`/innovate/sys/file/ask/query`),
            method: 'get',
            params: this.$http.adornParams({
              'fileAskType': 12,
              'fileAskTime': new Date().getFullYear()
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.fileAskContent = data.fileAsk == null ? '无' : data.fileAsk.fileAskContent
              this.dataListLoading = false
            }
          })
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (this.attachLists.length < 1) {
            this.fileIsNull = true
            this.addLoading = false
            return
          }
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovatecooperationmaterials/${!this.dataForm.materialsId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'materialsId': this.dataForm.materialsId || undefined,
                'functionId': this.dataForm.functionId,
                'attachName': this.dataForm.attachName,
                'attachPath': this.dataForm.attachPath,
                'attachTime': this.dataForm.attachTime,
                'innovateCooperationMaterialsEntities': this.attachLists,
                'isDel': this.dataForm.isDel
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
      successHandle1 (response, file, fileList) {
        if (response && response.code === 0) {
          this.attachLists.push(response.innovateCooperationMaterialsEntity)
          this.fileIsNull = false
        } else {
          this.$message.error(response.msg)
        }
      },
      fileRemoveHandler (file, fileList) {
        // 移除attachList中的附件
        let tempFileList = []
        for (var index = 0; index < this.attachLists.length; index++) {
          if (this.attachLists[index].attachName !== file.name) {
            tempFileList.push(this.attachLists[index])
          }
        }
        this.attachLists = tempFileList
      }
    }
  }
</script>
