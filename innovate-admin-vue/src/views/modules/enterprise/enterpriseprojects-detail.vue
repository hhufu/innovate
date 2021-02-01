<template>
  <el-dialog
    title="详情"
    :close-on-click-modal="true"
    :visible.sync="visible"
    :append-to-body="true"
  >
    <table border="1" cellspacing="0" width="100%" class="table" style="table-layout: fixed">
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr class="contents" align="center">
        <th colspan="10" style="height: 3.2rem">
          企业项目信息
        </th>
      </tr>
      <tr align='center'>
        <th colspan="2">项目名称</th>
        <td colspan="8">{{dataForm.projectName}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">企业名称</th>
        <td colspan="8">{{dataForm.enterpriseName}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">负责人</th>
        <td colspan="8">{{dataForm.projectDirector}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">年度</th>
        <td colspan="8">{{dataForm.projectYear}}年</td>
      </tr>
      <tr align='center'>
        <th colspan="2">项目开始时间</th>
        <td colspan="8">{{parseTime(dataForm.projStartTime, '{y}年{m}月{d}日')}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">截止时间</th>
        <td colspan="8">{{parseTime(dataForm.projStopTime, '{y}年{m}月{d}日')}}</td>
      </tr>
      <!--附件开始-->
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr align="center" class="contents">
        <th colspan="10" style="height: 3.2rem">附件</th>
      </tr>
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr align='center'>
        <th colspan="7">附件名</th>
        <td colspan="3">操作</td>
      </tr>
      <template>
        <tr v-for="item in attachLists"
            align="center">
          <td colspan="7">{{item.attachName}}</td>
          <td colspan="3">
            <el-button @click="attachDown(item)" :loading="downloadLoading"><span v-text="downloadText"></span>
            </el-button>
          </td>
        </tr>
      </template>
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <!--附件结束-->
    </table>
  </el-dialog>
</template>

<script>
  export default {
    data() {
      return {
        visible: false,
        downloadLoading: false,
        dataForm: {
          enterpProjId: 0,
          enterpriseId: '',
          enterpriseName: '',

          projectName: '',
          projStartTime: '',
          projStopTime: '',
          projectYear: '',
          projectDirector: '',
          projectUserId: '',
          isDel: 0
        },
        projectName: [],
        loading: false,
        fileIsNull: false,
        fileList: [],
        url: '',
        downloadText: '下载',
        attachLists: []
      }
    },
    methods: {
      init(id) {
        this.url = this.$http.adornUrl(
          `/enterprise/innovateenterpriseattach/upload?token=${this.$cookie.get(
            'token'
          )}`
        )
        this.dataForm.enterpProjId = id || 0
        this.visible = true
        this.$nextTick(() => {
          if (this.dataForm.enterpProjId) {
            this.$http({
              url: this.$http.adornUrl(
                `/enterprise/innovateenterpriseproject/info/${
                  this.dataForm.enterpProjId
                }`
              ),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.enterpriseId =
                  data.infoModel.projectEntity.enterpriseId
                this.dataForm.enterpriseName =
                  data.infoModel.projectEntity.enterpriseName
                this.dataForm.projectName =
                  data.infoModel.projectEntity.projectName
                this.dataForm.projStartTime =
                  data.infoModel.projectEntity.projStartTime
                this.dataForm.projStopTime =
                  data.infoModel.projectEntity.projStopTime
                this.dataForm.projectYear =
                  data.infoModel.projectEntity.projectYear
                this.dataForm.projectDirector =
                  data.infoModel.projectEntity.projectDirector
                this.dataForm.projectUserId =
                  data.infoModel.projectEntity.projectUserId
                this.dataForm.isDel = data.infoModel.projectEntity.isDel
                this.attachLists = data.infoModel.attachEntities
              }
            })
          }
        })
      },
      // 文件下载
      attachDown(attach) {
        this.downloadLoading = true
        this.downloadText = '正在下载'
        this.$notify({
          title: '下载提示',
          message: '文件正在后台下载,请您稍后!',
          duration: 0,
          type: 'success'
        })
        this.$httpFile({
          url: this.$httpFile.adornUrl(`/enterprise/innovateenterpriseattach/download`),
          method: 'post',
          params: this.$httpFile.adornParams({
            'filePath': attach.attachPath
          })
        }).then(response => {
          if (!response) {
            this.$notify({
              title: '下载提示',
              message: '下载失败',
              duration: 0,
              type: 'error'
            })
            this.downloadLoading = false
            return
          }
          let url = window.URL.createObjectURL(new Blob([response.data]))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', attach.attachName)
          document.body.appendChild(link)
          link.click()
          this.$notify({
            title: '下载成功',
            message: '后台文件下载成功',
            duration: 0,
            type: 'success'
          })
          this.downloadText = '下载'
          this.downloadLoading = false
        }).catch(err => {
          console.log(err)
          this.downloadLoading = false
        })
      }
    }
  }
</script>

<style scoped>

</style>
