<template>
  <el-dialog
    append-to-body
    :title="!id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <table border="1" cellspacing="0" width="100%" class="table">
      <tr class="contents" align="center">
        <th colspan="24">项目评分要点</th>
      </tr>
      <tr align='center' style="height: 3rem">
        <th colspan="17">评审要点文件名</th>
        <th colspan="7">操作</th>
      </tr>
      <tr align="center" v-if="attachEntity != null && attachEntity.attachName !=null ">
        <td colspan="17" align="center"><el-tag type="small" v-text="attachEntity.attachName"></el-tag></td>
        <td colspan="7" align="center"><el-button v-if="attachEntity.attachPath!==''" @click="attachDown(attachEntity)">下载</el-button></td>
      </tr>
    </table>
    <br>
    <br>
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="8rem">
      <el-form-item label="项目打分" prop="score">
        <el-input v-model="dataForm.score" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="格式打分" prop="score">
        <el-input v-model="dataForm.gsScore" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item label="反馈意见" prop="opinion">
        <el-input
          type="textarea"
          :rows="5"
          placeholder="请输入"
          v-model="dataForm.opinion">
        </el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { isFloatNumber } from '@/utils/validate'
  export default {
    name: 'score-add-or-update',
    data () {
      var validateFloatNumber = (rule, value, callback) => {
        if (!isFloatNumber(value)) {
          callback(new Error('至少保留小数点后一位'))
        } else {
          callback()
        }
      }
      return {
        visible: false,
        attachEntity: {},
        loading: false,
        userMap: [],
        id: 0,
        dataForm: {
          reviewId: '',
          apply: 'project_audit_apply_status',
          userId: this.$store.state.user.id,
          declareId: this.id,
          score: '',
          opinion: '',
          gsScore: ''
        },
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataRule: {
          score: [
            { required: true, message: '分数不能为空', trigger: 'blur' },
            { validator: validateFloatNumber, trigger: 'blur' }
          ],
          gsScore: [
            { required: true, message: '分数不能为空', trigger: 'blur' },
            { validator: validateFloatNumber, trigger: 'blur' }
          ],
          opinion: [
            { required: true, message: '意见反馈不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (index, e) {
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          this.id = index || 0
          this.$http({
            url: this.$http.adornUrl(`/innovate/innovatedeclarationprocesssetting/queryByTime`),
            method: 'get',
            params: this.$http.adornParams({
              declareProcessName: 1, // 结题为2 立项为1
              time: e.declareTime
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.attachEntity = data.declarationProcessSetting
            }
          })
          if (this.id) {
            this.$http({
              url: this.$http.adornUrl(`/innovate/declare/review/info`),
              method: 'get',
              params: this.$http.adornParams({
                'apply': 'project_audit_apply_status',
                'userId': this.$store.state.user.id,
                'declareId': this.id,
                'isDel': 0
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm = data.declareReviewEntity
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
              url: this.$http.adornUrl(`/innovate/declare/review/score`),
              method: 'post',
              data: this.$http.adornData(this.dataForm)
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
      attachDown (attach) {
        this.downloadLoading = true
        this.$http({
          url: this.$http.adornUrl(`/innovate/declare/attach/downloadurl`),
          method: 'post',
          params: this.$http.adornParams({
            'filePath': attach.attachPath
          })
        }).then(({data}) => {
          window.open(data.url)
          this.downloadLoading = false
        }).catch(err => {
          this.downloadLoading = false
          console.log(err)
        })

      },
    }
  }
</script>
<style>
.table {
  border: 1px solid #ddd;
  /*为表格设置合并边框模型*/
  border-collapse: collapse;
  /*列宽由表格宽度和列宽度设定*/
  table-layout: fixed;
}
</style>
