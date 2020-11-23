<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="参加类型" prop="participateType">
      <el-input v-model="dataForm.participateType" placeholder="参加类型"></el-input>
    </el-form-item>
    <el-form-item label="比赛级别（等级、项目）" prop="raceGrade">
      <el-input v-model="dataForm.raceGrade" placeholder="比赛级别（等级、项目）"></el-input>
    </el-form-item>
    <el-form-item label="奖项等级" prop="prizeGrade">
      <el-input v-model="dataForm.prizeGrade" placeholder="奖项等级"></el-input>
    </el-form-item>
    <el-form-item label="参与人类别（1：负责人，2参与成员）" prop="persionType">
      <el-input v-model="dataForm.persionType" placeholder="参与人类别（1：负责人，2参与成员）"></el-input>
    </el-form-item>
    <el-form-item label="积分" prop="integral">
      <el-input v-model="dataForm.integral" placeholder="积分"></el-input>
    </el-form-item>
    <el-form-item label="是否删除" prop="isDel">
      <el-input v-model="dataForm.isDel" placeholder="是否删除"></el-input>
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
          integralId: 0,
          participateType: '',
          raceGrade: '',
          prizeGrade: '',
          persionType: '',
          integral: '',
          isDel: ''
        },
        dataRule: {
          participateType: [
            { required: true, message: '参加类型不能为空', trigger: 'blur' }
          ],
          raceGrade: [
            { required: true, message: '比赛级别（等级、项目）不能为空', trigger: 'blur' }
          ],
          prizeGrade: [
            { required: true, message: '奖项等级不能为空', trigger: 'blur' }
          ],
          persionType: [
            { required: true, message: '参与人类别（1：负责人，2参与成员）不能为空', trigger: 'blur' }
          ],
          integral: [
            { required: true, message: '积分不能为空', trigger: 'blur' }
          ],
          isDel: [
            { required: true, message: '是否删除不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.integralId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.integralId) {
            this.$http({
              url: this.$http.adornUrl(`/points/innovatesyspoints/info/${this.dataForm.integralId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.participateType = data.innovatesyspoints.participateType
                this.dataForm.raceGrade = data.innovatesyspoints.raceGrade
                this.dataForm.prizeGrade = data.innovatesyspoints.prizeGrade
                this.dataForm.persionType = data.innovatesyspoints.persionType
                this.dataForm.integral = data.innovatesyspoints.integral
                this.dataForm.isDel = data.innovatesyspoints.isDel
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
              url: this.$http.adornUrl(`/points/innovatesyspoints/${!this.dataForm.integralId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'integralId': this.dataForm.integralId || undefined,
                'participateType': this.dataForm.participateType,
                'raceGrade': this.dataForm.raceGrade,
                'prizeGrade': this.dataForm.prizeGrade,
                'persionType': this.dataForm.persionType,
                'integral': this.dataForm.integral,
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
      }
    }
  }
</script>
